package org.hage.platform.component.workplace;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.selector.AddressSelector;
import org.hage.platform.communication.address.selector.Selectors;
import org.hage.platform.communication.message.Message;
import org.hage.platform.component.action.Action;
import org.hage.platform.component.action.IActionContext;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.context.*;
import org.hage.platform.component.agent.*;
import org.hage.platform.component.execution.IllegalOperationException;
import org.hage.platform.component.execution.WorkplaceException;
import org.hage.platform.component.query.AgentEnvironmentQuery;
import org.hage.platform.component.query.EnvironmentAddressesQuery;
import org.hage.platform.component.query.IQuery;
import org.hage.platform.component.workplace.manager.WorkplaceManagerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Queues.newConcurrentLinkedQueue;


public class SimpleWorkplace implements Workplace, ISimpleAgentEnvironment {

    private static final Logger log = LoggerFactory.getLogger(SimpleWorkplace.class);

    private static final int QUERY_EXECUTOR_RATE_MS = 500;

    @Nonnull
    private final AtomicLong step = new AtomicLong(0);

    @Nonnull
    private final Object stateMonitor = new Object();
    private final Queue<Action> actionQueue = newConcurrentLinkedQueue();
    private ISimpleAgent agent;
    private final Runnable stepExecutorRunnable = new Runnable() {

        @Override
        public void run() {
            setRunning();
            log.info("{} has been started.", this);

            while(isRunning() || isPaused()) {
                log.debug("Step {} on {}.", step, agent);
                agent.step();

                processActions();
                step.getAndIncrement();
            }

            agent.finish();
        }
    };
    private List<IQuery<Object, Object>> queries;
    @GuardedBy("stateMonitor")
    @Nonnull
    private State state = State.STOPPED;
    @CheckForNull
    private WorkplaceEnvironment environment;
    private final Runnable queryCacheExecutorRunnable = new Runnable() {

        @Override
        public void run() {
            for(final IQuery<Object, Object> query : queries) {
                log.debug("Query: {}", query);
                final Iterable<?> execute = (Iterable<?>) query.execute(Collections.singleton(getAgent()));
                log.debug("Query result: {}", execute);
                environment.cacheQueryResults(getAddress(), query, execute);
            }
        }
    };
    private ListeningScheduledExecutorService executorService;
    private ListenableFuture<?> agentStepExecutorFuture;
    private ListenableScheduledFuture<?> queryExecutorFuture;

    /**
     * Gets the step.
     *
     * @return the step
     */
    public long getStep() {
        return step.get();
    }

    /**
     * Sets the state of the workplace to "running".
     */
    protected void setRunning() {
        synchronized(stateMonitor) {
            state = State.RUNNING;
        }
    }

    /**
     * Sets the state of the workplace to "stopped".
     */
    protected void setStopped() {
        synchronized(stateMonitor) {
            if(isStopped()) {
                return;
            }
            state = State.STOPPED;
        }
        log.info("Calling environment.");
        getEnvironment().onWorkplaceStop(this);
        log.info("{} stopped", this);
    }

    /**
     * Returns the environment of the workplace.
     *
     * @return the environment.
     */
    @Nonnull
    protected final WorkplaceEnvironment getEnvironment() {
        checkState(environment != null);
        return environment;
    }

    @Override
    public void setEnvironment(@CheckForNull final WorkplaceEnvironment environment) {
        if(environment == null) {
            this.environment = null;
        } else if(this.environment == null) {
            this.environment = environment;
        } else {
            throw new WorkplaceException(String.format("Environment in %s is already set.", this));
        }
    }

    @Override
    public void start() {
        log.info("{} is starting...", this);

        executorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(2,
                                                                                            (new ThreadFactoryBuilder()).setNameFormat("wp-" + getAddress().getFriendlyName() + "-%d").build()));
        queryExecutorFuture = executorService.scheduleAtFixedRate(queryCacheExecutorRunnable, 0, QUERY_EXECUTOR_RATE_MS,
                                                                  TimeUnit.MILLISECONDS);
        if(queries == null) {
            setQueries(Lists.<IQuery<Object, Object>> newArrayList());
        }
        checkState(isStopped(), "Workplace has been already started.");

        agentStepExecutorFuture = executorService.submit(stepExecutorRunnable);
        Futures.addCallback(agentStepExecutorFuture, new AgentStepExecutorCallback(),
                            MoreExecutors.sameThreadExecutor());
        Futures.addCallback(queryExecutorFuture, new QueryExecutorCallback(), MoreExecutors.sameThreadExecutor());

        // "Running" state is set in the step executor.
    }

    @Override
    public void pause() {
        log.debug("{} asked to pause.", getAddress());
        synchronized(stateMonitor) {
            checkState(isRunning(), "Workplace is not running.");
            state = State.PAUSED;
        }

        agentStepExecutorFuture.cancel(false);
    }

    @Override
    public void resume() {
        log.debug("{} asked to resume.", this);
        synchronized(stateMonitor) {
            checkState(isPaused(), "Workplace is not paused.");
            state = State.RUNNING;
        }

        agentStepExecutorFuture = executorService.submit(stepExecutorRunnable);
        Futures.addCallback(agentStepExecutorFuture, new AgentStepExecutorCallback(),
                            MoreExecutors.sameThreadExecutor());
    }

    @Override
    public void stop() {
        log.debug("{} asked to stop.", this);
        synchronized(stateMonitor) {
            checkState(isRunning() || isPaused(), "Workplace is not running or paused.");
            state = State.STOPPING;
        }

        executorService.shutdown();

    }

    @Override
    public boolean isRunning() {
        synchronized(stateMonitor) {
            return State.RUNNING.equals(state);
        }
    }

    @Override
    public boolean isPaused() {
        synchronized(stateMonitor) {
            return State.PAUSED.equals(state);
        }
    }

    @Override
    public boolean isStopped() {
        synchronized(stateMonitor) {
            return State.STOPPED.equals(state);
        }
    }

    @Override
    public void deliverMessage(@Nonnull final Message<AgentAddress, ?> message) {
        if(isStopped()) {
            log.debug("Workplace is stopped and ignores all messages.");
            return;
        }
        agent.deliverMessage(message);
    }

    @Override
    public void sendAgent(@Nonnull final IAgent migrant) {
        log.debug("Incoming migrant: {}.", migrant);

        checkArgument(migrant instanceof ISimpleAgent);
        checkState(agent instanceof ISimpleAggregate);
        if(isStopped()) {
            log.debug("Workplace is stopped and ignores all migrations.");
            return;
        }
        final ISimpleAggregate aggregate = (ISimpleAggregate) agent;
        aggregate.add((ISimpleAgent) migrant);
    }

    @Nonnull
    public ISimpleAgent getAgent() {
        checkState(agent != null);
        return agent;
    }

	/* Actions translation to distributed environment */

    public void setQueries(final List<IQuery<Object, Object>> queries) {
        this.queries = queries;
        this.queries.add((IQuery) new EnvironmentAddressesQuery());
    }

    @Override
    public <E extends IAgent, T> Collection<T> queryParent(final AgentEnvironmentQuery<E, T> query) {
        throw new IllegalOperationException("Agent has no grandparents.");
    }

    @Override
    public <E extends IAgent, T> Collection<T> query(final AgentEnvironmentQuery<E, T> query) {
        log.debug("Query on workplaces: {}.", query);
        return getEnvironment().queryWorkplaces(query);
    }

    @Override
    @Nonnull
    public AgentAddress getAddress() {
        return getAgent().getAddress();
    }

    public void setAgent(final @Nonnull ISimpleAgent agent) {
        log.debug("Agent set: {}.", agent);
        this.agent = agent;
        this.agent.setAgentEnvironment(this);
    }

    @Override
    public void submitAction(final Action action) {
        log.debug("Action: {}.", action);
        actionQueue.add(action);
    }

    protected void processActions() {
        for(final Action action : consumingIterable(actionQueue)) {
            for(final SingleAction singleAction : action) {
                final IActionContext context = singleAction.getContext();
                final AddressSelector<AgentAddress> targetSelector = singleAction.getTarget();

                // Ugly code ;(
                if(context instanceof SendMessageActionContext) {
                    handleAction(targetSelector, (SendMessageActionContext) context);
                } else if(context instanceof PassToParentActionContext) {
                    handleAction(targetSelector, (PassToParentActionContext) context);
                } else if(context instanceof MoveAgentActionContext) {
                    handleAction(targetSelector, (MoveAgentActionContext) context);
                } else if(context instanceof RemoveAgentActionContext) {
                    handleAction(targetSelector, (RemoveAgentActionContext) context);
                } else if(context instanceof KillAgentActionContext) {
                    handleAction(targetSelector, (KillAgentActionContext) context);
                } else if(context instanceof AddAgentActionContext) {
                    handleAction(targetSelector, (AddAgentActionContext) context);
                }

            }
        }
    }

    private void handleAction(@Nonnull final AddressSelector<AgentAddress> targetSelector,
            @Nonnull final SendMessageActionContext context) {
        final WorkplaceManagerMessage message =
                WorkplaceManagerMessage.create(WorkplaceManagerMessage.MessageType.AGENT_MESSAGE, context.getMessage());
        getEnvironment().sendMessage(message);
    }

    private void handleAction(@Nonnull final AddressSelector<AgentAddress> targetSelector,
            @Nonnull final PassToParentActionContext context) {
        throw new AgentException("Pass to parent impossible for top-level agents.");
    }

    private void handleAction(@Nonnull final AddressSelector<AgentAddress> targetSelector,
            @Nonnull final MoveAgentActionContext context) {

        final ISimpleAgent migrant = context.getAgent();
        if(migrant instanceof IAggregate<?>) {
            throw new AgentException("Cannot move aggregates yet.");
        }
        final AgentAddress migrantAddress = migrant.getAddress();
        final ISimpleAggregate migrantParent = context.getParent();

        try {
            migrantParent.removeAgent(migrantAddress);
        } catch(final AgentException e) {
            log.info("Cannot remove the agent [agent: {}, parent: {}]", migrantAddress, migrantParent.getAddress());
            throw e;
        }

        final ImmutableMap<String, Serializable> migrationPayload =
                (new ImmutableMap.Builder<String, Serializable>()).put("target", targetSelector)
                        .put("agent", migrant)
                        .build();

        final WorkplaceManagerMessage message =
                WorkplaceManagerMessage.create(WorkplaceManagerMessage.MessageType.MIGRATE_AGENT, migrationPayload);
        getEnvironment().sendMessage(message);
    }

    private void handleAction(@Nonnull final AddressSelector<AgentAddress> targetSelector,
            @Nonnull final RemoveAgentActionContext context) {
        final Set<AgentAddress> addresses = getEnvironment().getAddressesOfWorkplaces();
        for(final AgentAddress address : Selectors.filter(addresses, targetSelector)) {
            getEnvironment().requestRemoval(address);
        }
    }

    private void handleAction(@Nonnull AddressSelector<AgentAddress> targetSelector,
            @Nonnull final KillAgentActionContext context) {
        final Set<AgentAddress> addresses = getEnvironment().getAddressesOfWorkplaces();
        for(final AgentAddress address : Selectors.filter(addresses, targetSelector)) {
            getEnvironment().requestRemoval(address);
        }
    }

    private void handleAction(@Nonnull AddressSelector<AgentAddress> targetSelector,
            @Nonnull final AddAgentActionContext context) {
        throw new AgentException("Creation of workplaces is not implemented yet."); // XXX:
    }

    @Override
    public String toString() {
        final Objects.ToStringHelper helper = toStringHelper(this);
        synchronized(stateMonitor) {
            helper.add("state", state);
        }
        return helper.add("step", step.get()).add("agent", agent).toString();
    }


    private static class QueryExecutorCallback implements FutureCallback<Object> {

        @Override
        public void onSuccess(final Object result) {
            log.debug("Successful cache.");
        }

        @Override
        public void onFailure(final Throwable t) {
            log.error("Exception caught during cache run.", t);
        }
    }


    private class AgentStepExecutorCallback implements FutureCallback<Object> {

        @Override
        public void onSuccess(final Object result) {
            log.debug("Successful computation.");
            setStopped();
        }

        @Override
        public void onFailure(final Throwable t) {
            log.error("Exception caught during run.", t);
            setStopped();
        }
    }
}
