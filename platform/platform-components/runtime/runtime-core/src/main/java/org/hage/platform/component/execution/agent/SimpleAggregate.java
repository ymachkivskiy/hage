package org.hage.platform.component.execution.agent;


import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.communication.message.Message;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.execution.action.Action;
import org.hage.platform.component.execution.action.SingleAction;
import org.hage.platform.component.execution.query.AgentEnvironmentQuery;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.hage.property.PropertyGetter;
import org.hage.property.PropertySetter;
import org.hage.util.Locks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;


public class SimpleAggregate extends SimpleAgent implements ISimpleAggregate, ISimpleAgentEnvironment,
        IComponentInstanceProviderAware {

    // Important: this class should contain only collection-like methods and simple, forwarding methods required by
    // the ISimpleAgentEnvironment.

    private static final long serialVersionUID = 5L;

    private static final Logger log = LoggerFactory.getLogger(SimpleAggregate.class);

    /**
     * The map containing aggregate's children.
     */
    // A linked hashmap is needed to ensure deterministic iteration order over the aggregate
    protected final Map<AgentAddress, ISimpleAgent> agents = newLinkedHashMap();
    /**
     * The list of agents set by method {@link #setAgents(List)}. Agents from this list will be added to the map of
     * agents in the {@link #init()} method.
     */
    protected List<ISimpleAgent> temporaryAgentsList;
    /**
     * The lock for accessing the {@link #agents} map.
     */
    transient private ReentrantReadWriteLock agentsLock = new ReentrantReadWriteLock();
    @Inject
    transient private AggregateQueryService queryService;

    @Inject
    transient private AggregateActionService actionService;

    @Inject
    transient private AggregateMessagingService messagingService;

    transient private IComponentInstanceProvider instanceProvider;

    public SimpleAggregate(final AgentAddress address) {
        super(address);
    }

    @Inject
    public SimpleAggregate(final AgentAddressSupplier supplier) {
        super(supplier);
    }

	/* Lifecycle methods. */

    @Override
    public void init() {
        super.init();
        queryService.setAggregate(this);
        actionService.setAggregate(this);
        actionService.setInstanceProvider(instanceProvider);
    }

    @Override
    public boolean finish() {
        withReadLock(() -> {
            for (final ISimpleAgent agent : agents.values()) {
                try {
                    agent.finish();
                } catch (final ComponentException e) {
                    log.error("Finalization of {} resulted in an exception.", agent, e);
                }
            }
        });
        return super.finish();
    }

    protected final void withReadLock(final Runnable action) {
        assert (action != null);
        try {
            Locks.withReadLock(agentsLock, action);
        } catch (final Exception e) {
            log.warn("Interrupted.", e);
        }
    }

    @Override
    public void step() {

        withReadLock(() -> {
            log.debug("Step on agents {}.", agents);
            agents.values().forEach(ISimpleAgent::step);
        });

        actionService.processActions();
        notifyMonitorsForChangedProperties();
    }

	/* Aggregate by-address methods. */

    @Override
    public void setAgentEnvironment(final IAgentEnvironment localEnvironment) {
        super.setAgentEnvironment(localEnvironment);

        actionService.processActions();
        if (temporaryAgentsList != null) {
            addAll(temporaryAgentsList);
            temporaryAgentsList = null;
        }
    }

    @Nullable
    protected final <V> V withWriteLock(final Callable<V> action) {
        assert (action != null);
        try {
            return Locks.withWriteLock(agentsLock, action);
        } catch (final InterruptedException e) {
            log.warn("Interrupted.", e);
            Thread.currentThread().interrupt();
            return null;
        } catch (final Exception e) {
            log.error("An exception was thrown when having a lock.", e);
            throw Throwables.propagate(e);
        }
    }

    private boolean addAgentWithoutSynchronization(final ISimpleAgent agent) {
        assert (agent != null);
        try {
            final AgentAddress agentAddress = agent.getAddress();

            // Check whether another *instance* of agent exists - possible error?
            if (agents.containsKey(agentAddress) && agents.get(agentAddress) != agent) {
                log.error("Another instance of the agent with address '{}' already exists in this environment.",
                        agentAddress);
                return false;
            }
            agents.put(agentAddress, agent);
            agent.setAgentEnvironment(this);

            log.debug("Agent {} added to the aggregate {}.", agentAddress, getAddress());
            return true;
        } catch (final AgentException e) {
            log.error("Agent already exist in this environment.", e);
            return false;
        }
    }

    @Override
    public final boolean containsAgent(final AgentAddress agentAddress) {
        checkNotNull(agentAddress);
        return withReadLock(() -> agents.containsKey(agentAddress));
    }

	/* Setter and getter for the initial agent list. */

    @Override
    public final ISimpleAgent getAgent(final AgentAddress agentAddress) {
        checkNotNull(agentAddress);
        return withReadLock(() -> agents.get(agentAddress));
    }

    @Override
    public final void removeAgent(final AgentAddress agentAddress) {
        checkNotNull(agentAddress);
        withWriteLock(() -> removeAgentWithoutSynchronization(agentAddress));
    }

    @Override
    public final Set<AgentAddress> getAgentsAddresses() {
        return withReadLock(() -> ImmutableSet.copyOf(agents.keySet()));
    }

    private boolean removeAgentWithoutSynchronization(final AgentAddress agentAddress) {
        assert (agentAddress != null);
        try {
            final IAgent agent = agents.remove(agentAddress);
            if (agent != null) {
                agent.setAgentEnvironment(null);
                log.debug("Agent {} removed from the aggregate {}.", agent.getAddress(), getAddress());
            }
        } catch (final AgentException e) {
            log.error("Can't unset agent's environment.", e);
        }
        return true;
    }

    @Nullable
    protected final <V> V withReadLock(final Callable<V> action) {
        assert (action != null);
        try {
            return Locks.withReadLock(agentsLock, action);
        } catch (final InterruptedException e) {
            log.warn("Interrupted.", e);
            Thread.currentThread().interrupt();
            return null;
        } catch (final Exception e) {
            log.error("An exception was thrown when having a lock.", e);
            throw Throwables.propagate(e);
        }
    }

    @PropertySetter(propertyName = "agents")
    public final void setAgents(final List<ISimpleAgent> agents) {
        temporaryAgentsList = agents;
        log.debug("Agents added.");
    }

    @PropertyGetter(propertyName = "agents")
    public final List<ISimpleAgent> getAgents() {
        return withReadLock(() -> ImmutableList.copyOf(agents.values()));
    }

    @Override
    public final int size() {
        return withReadLock(() -> agents.size());
    }

    @Override
    public final boolean isEmpty() {
        return withReadLock(() -> agents.isEmpty());
    }

    @Override
    public final boolean contains(final Object o) {
        checkNotNull(o);
        return withReadLock(() -> agents.containsValue(o));
    }

    @Override
    @Nonnull
    public final Iterator<ISimpleAgent> iterator() {
        return withReadLock(() -> ImmutableSet.copyOf(agents.values()).iterator());
    }

    @Override
    public final Object[] toArray() {
        return withReadLock(() -> agents.values().toArray());
    }

    @Override
    public final <T> T[] toArray(final T[] a) {
        checkNotNull(a);
        return withReadLock(() -> agents.values().toArray(a));
    }

    @Override
    public final boolean add(final ISimpleAgent agent) {
        checkNotNull(agent);
        return withWriteLock(() -> addAgentWithoutSynchronization(agent));
    }

    @Override
    public final boolean remove(final Object o) {
        checkNotNull(o);
        checkArgument(o instanceof ISimpleAgent);
        return withWriteLock(() -> removeAgentWithoutSynchronization(((ISimpleAgent) o).getAddress()));
    }

	/* Private utility methods. */

    @Override
    public final boolean containsAll(final Collection<?> c) {
        checkNotNull(c);
        return withReadLock(() -> agents.values().containsAll(c));
    }

    @Override
    public final boolean addAll(@Nonnull final Collection<? extends ISimpleAgent> agentsToAdd) {
        checkNotNull(agentsToAdd);
        return withWriteLock(() -> {
            for (final ISimpleAgent agent : agentsToAdd) {
                if (!addAgentWithoutSynchronization(agent)) {
                    return false;
                }
            }
            return true;
        });
    }

	/* Aggregate services - delegations */

    @Override
    public final boolean removeAll(final Collection<?> c) {
        checkNotNull(c);
        return withWriteLock(() -> agents.values().removeAll(c));
    }

    @Override
    public final boolean retainAll(final Collection<?> c) {
        checkNotNull(c);
        return withWriteLock(() -> agents.values().retainAll(c));
    }

    @Override
    public final void clear() {
        withWriteLock(new Runnable() {

            @Override
            public void run() {
                agents.clear();
            }
        });
    }

    protected final void withWriteLock(final Runnable action) {
        assert (action != null);
        try {
            Locks.withWriteLock(agentsLock, action);
        } catch (final Exception e) {
            log.warn("Interrupted.", e);
        }
    }

    @Override
    public <E extends IAgent, T> Collection<T> queryParent(final AgentEnvironmentQuery<E, T> query) {
        return queryService.queryParent(query);
    }

	/* Lock utilities. */

    @Override
    public <E extends IAgent, T> Collection<T> query(final AgentEnvironmentQuery<E, T> query) {
        return query.execute((Collection<E>) this);
    }

    @Override
    public void submitAction(final Action action) {
        actionService.doAction(action);
    }


    protected void sendMessage(final Message<AgentAddress, ?> message) {
        messagingService.sendMessage(message);
    }

    @Override
    public Collection<AgentAddress> validateAction(final SingleAction action) {
        return actionService.validateAction(action);
    }


    public AggregateQueryService getQueryService() {
        return queryService;
    }


    public void setQueryService(final AggregateQueryService queryService) {
        this.queryService = queryService;
    }


    public AggregateActionService getActionService() {
        return actionService;
    }

    public void setActionService(final AggregateActionService actionService) {
        this.actionService = actionService;
    }


    public AggregateMessagingService getMessagingService() {
        return messagingService;
    }


    void setMessagingService(final AggregateMessagingService messagingService) {
        this.messagingService = messagingService;
    }

    protected final ReentrantReadWriteLock getAgentsLock() {
        return agentsLock;
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.instanceProvider = provider;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        agentsLock = new ReentrantReadWriteLock();
    }
}
