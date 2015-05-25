/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2008-10-07
 * $Id$
 */

package org.jage.workplace;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.action.context.AddAgentActionContext;
import org.jage.action.context.KillAgentActionContext;
import org.jage.action.context.MoveAgentActionContext;
import org.jage.action.context.PassToParentActionContext;
import org.jage.action.context.RemoveAgentActionContext;
import org.jage.action.context.SendMessageActionContext;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.IAggregate;
import org.jage.agent.ISimpleAgent;
import org.jage.agent.ISimpleAgentEnvironment;
import org.jage.agent.ISimpleAggregate;
import org.jage.communication.message.Message;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.query.EnvironmentAddressesQuery;
import org.jage.query.IQuery;
import org.jage.workplace.manager.WorkplaceManagerMessage;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Queues.newConcurrentLinkedQueue;

/**
 * This is a basic workplace for simple agents ({@link ISimpleAgent}).
 *
 * @author AGH AgE Team
 */
public class SimpleWorkplace implements Workplace, ISimpleAgentEnvironment {

	private static final Logger log = LoggerFactory.getLogger(SimpleWorkplace.class);

	private static final int QUERY_EXECUTOR_RATE_MS = 500;

	@Nonnull
	private final AtomicLong step = new AtomicLong(0);

	@Nonnull
	private final Object stateMonitor = new Object();

	private ISimpleAgent agent;

	private List<IQuery<Object, Object>> queries;

	@GuardedBy("stateMonitor") @Nonnull
	private State state = State.STOPPED;

	@CheckForNull
	private WorkplaceEnvironment environment;

	private ListeningScheduledExecutorService executorService;

	private ListenableFuture<?> agentStepExecutorFuture;

	private ListenableScheduledFuture<?> queryExecutorFuture;

	private final Queue<Action> actionQueue = newConcurrentLinkedQueue();

	@Override
	public void start() {
		log.info("{} is starting...", this);

		executorService = MoreExecutors.listeningDecorator(Executors.newScheduledThreadPool(2,
				(new ThreadFactoryBuilder()).setNameFormat("wp-" + getAddress().getFriendlyName() + "-%d").build()));
		queryExecutorFuture = executorService.scheduleAtFixedRate(queryCacheExecutorRunnable, 0, QUERY_EXECUTOR_RATE_MS,
				TimeUnit.MILLISECONDS);
		if (queries == null) {
			setQueries(Lists.<IQuery<Object, Object>>newArrayList());
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
		synchronized (stateMonitor) {
			checkState(isRunning(), "Workplace is not running.");
			state = State.PAUSED;
		}

		agentStepExecutorFuture.cancel(false);
	}

	@Override
	public void resume() {
		log.debug("{} asked to resume.", this);
		synchronized (stateMonitor) {
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
		synchronized (stateMonitor) {
			checkState(isRunning() || isPaused(), "Workplace is not running or paused.");
			state = State.STOPPING;
		}

		try {
			if (!agentStepExecutorFuture.isDone()) {
				agentStepExecutorFuture.get();
			}
		} catch (final InterruptedException | ExecutionException e) {
			log.warn("Exception when waiting for step executor to finish.", e);
		}
	}

	@Override
	public boolean terminate() {
		checkState(isStopped(), "Illegal use of terminate. Must invoke stop() first.");

		queryExecutorFuture.cancel(true);
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.debug("Interrupted.", e);
		}
		log.info("{} has been shut down.", getAddress());
		return true;
	}

	@Override
	public boolean isRunning() {
		synchronized (stateMonitor) {
			return State.RUNNING.equals(state);
		}
	}

	@Override
	public boolean isPaused() {
		synchronized (stateMonitor) {
			return State.PAUSED.equals(state);
		}
	}

	@Override
	public boolean isStopped() {
		synchronized (stateMonitor) {
			return State.STOPPED.equals(state);
		}
	}

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
		synchronized (stateMonitor) {
			state = State.RUNNING;
		}
	}

	/**
	 * Sets the state of the workplace to "stopped".
	 */
	protected void setStopped() {
		synchronized (stateMonitor) {
			if (isStopped()) {
				return;
			}
			state = State.STOPPED;
		}
		log.info("Calling environment.");
		getEnvironment().onWorkplaceStop(this);
		log.info("{} stopped", this);
	}

	@Override
	public void setEnvironment(@CheckForNull final WorkplaceEnvironment environment) {
		if (environment == null) {
			this.environment = null;
		} else if (this.environment == null) {
			this.environment = environment;
		} else {
			throw new WorkplaceException(String.format("Environment in %s is already set.", this));
		}
	}

	/**
	 * Returns the environment of the workplace.
	 *
	 * @return the environment.
	 */
	@Nonnull protected final WorkplaceEnvironment getEnvironment() {
		checkState(environment != null);
		return environment;
	}

	@Override public <E extends IAgent, T> Collection<T> queryParent(final AgentEnvironmentQuery<E, T> query) {
		throw new IllegalOperationException("Agent has no grandparents.");
	}

	@Override public <E extends IAgent, T> Collection<T> query(final AgentEnvironmentQuery<E, T> query) {
		log.debug("Query on workplaces: {}.", query);
		return getEnvironment().queryWorkplaces(query);
	}

	@Override @Nonnull public AgentAddress getAddress() {
		return getAgent().getAddress();
	}

	@Nonnull public ISimpleAgent getAgent() {
		checkState(agent != null);
		return agent;
	}

	public void setAgent(final @Nonnull ISimpleAgent agent) {
		log.debug("Agent set: {}.", agent);
		this.agent = agent;
		this.agent.setAgentEnvironment(this);
	}

	/* Actions translation to distributed environment */

	@Override public void submitAction(final Action action) {
		log.debug("Action: {}.", action);
		actionQueue.add(action);
	}

	protected void processActions() {
		for (final Action action : consumingIterable(actionQueue)) {
			for (final SingleAction singleAction : action) {
				final IActionContext context = singleAction.getContext();
				final AddressSelector<AgentAddress> targetSelector = singleAction.getTarget();

				// Ugly code ;(
				if (context instanceof SendMessageActionContext) {
					handleAction(targetSelector, (SendMessageActionContext)context);
				} else if (context instanceof PassToParentActionContext) {
					handleAction(targetSelector, (PassToParentActionContext)context);
				} else if (context instanceof MoveAgentActionContext) {
					handleAction(targetSelector, (MoveAgentActionContext)context);
				} else if (context instanceof RemoveAgentActionContext) {
					handleAction(targetSelector, (RemoveAgentActionContext)context);
				} else if (context instanceof KillAgentActionContext) {
					handleAction(targetSelector, (KillAgentActionContext)context);
				} else if (context instanceof AddAgentActionContext) {
					handleAction(targetSelector, (AddAgentActionContext)context);
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
		if (migrant instanceof IAggregate<?>) {
			throw new AgentException("Cannot move aggregates yet.");
		}
		final AgentAddress migrantAddress = migrant.getAddress();
		final ISimpleAggregate migrantParent = context.getParent();

		try {
			migrantParent.removeAgent(migrantAddress);
		} catch (final AgentException e) {
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
		for (final AgentAddress address : Selectors.filter(addresses, targetSelector)) {
			getEnvironment().requestRemoval(address);
		}
	}

	private void handleAction(@Nonnull AddressSelector<AgentAddress> targetSelector,
			@Nonnull final KillAgentActionContext context) {
		final Set<AgentAddress> addresses = getEnvironment().getAddressesOfWorkplaces();
		for (final AgentAddress address : Selectors.filter(addresses, targetSelector)) {
			getEnvironment().requestRemoval(address);
		}
	}

	private void handleAction(@Nonnull AddressSelector<AgentAddress> targetSelector,
			@Nonnull final AddAgentActionContext context) {
		throw new AgentException("Creation of workplaces is not implemented yet."); // XXX:
	}

	@Override public void deliverMessage(@Nonnull final Message<AgentAddress, ?> message) {
		if (isStopped()) {
			log.debug("Workplace is stopped and ignores all messages.");
			return;
		}
		agent.deliverMessage(message);
	}

	@Override public void sendAgent(@Nonnull final IAgent migrant) {
		log.debug("Incoming migrant: {}.", migrant);

		checkArgument(migrant instanceof ISimpleAgent);
		checkState(agent instanceof ISimpleAggregate);
		if (isStopped()) {
			log.debug("Workplace is stopped and ignores all migrations.");
			return;
		}
		final ISimpleAggregate aggregate = (ISimpleAggregate)agent;
		aggregate.add((ISimpleAgent)migrant);
	}

	public void setQueries(final List<IQuery<Object, Object>> queries) {
		this.queries = queries;
		this.queries.add((IQuery)new EnvironmentAddressesQuery());
	}

	@Override public String toString() {
		final Objects.ToStringHelper helper = toStringHelper(this);
		synchronized (stateMonitor) {
			helper.add("state", state);
		}
		return helper.add("step", step.get()).add("agent", agent).toString();
	}

	private final Runnable stepExecutorRunnable = new Runnable() {
		@Override public void run() {
			setRunning();
			log.info("{} has been started.", this);

			while (isRunning() || isPaused()) {
				log.debug("Step {} on {}.", step, agent);
				agent.step();

				processActions();
				step.getAndIncrement();
			}

			agent.finish();
		}
	};

	private final Runnable queryCacheExecutorRunnable = new Runnable() {
		@Override public void run() {
			for (final IQuery<Object, Object> query : queries) {
				log.debug("Query: {}", query);
				final Iterable<?> execute = (Iterable<?>)query.execute(Collections.singleton(getAgent()));
				log.debug("Query result: {}", execute);
				environment.cacheQueryResults(getAddress(), query, execute);
			}
		}
	};

	private static class QueryExecutorCallback implements FutureCallback<Object> {
		@Override public void onSuccess(final Object result) {
			log.debug("Successful cache.");
		}

		@Override public void onFailure(final Throwable t) {
			log.error("Exception caught during cache run.", t);
		}
	}

	private class AgentStepExecutorCallback implements FutureCallback<Object> {
		@Override public void onSuccess(final Object result) {
			log.debug("Successful computation.");
			setStopped();
		}

		@Override public void onFailure(final Throwable t) {
			log.error("Exception caught during run.", t);
			setStopped();
		}
	}
}
