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

package org.jage.agent;


import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.communication.message.Message;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.util.Locks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;


/**
 * The basic implementation of an aggregate for simple agents.
 * <p>
 * <p>
 * SimpleAggregate is an unsorted set of agents of the {@link ISimpleAgent} type. However, the equality of agents is
 * always based on their addresses and not on the {@code equals} relation. (Note, that it is possible, although not
 * recommended to base the agent equality on its other properties).
 * <p>
 * <p>
 * All agents are available also using their addresses ({@link #getAgent(AgentAddress)}).
 * <p>
 * <p>
 * This class relies on so-called "aggregate services" injected into it. These services are delegates implementing
 * aggregate functions that are out of scope of simple collection wrapping. If new functionality is needed, instead of
 * inheriting from this class, it is recommended to extend a specific service.
 *
 * @author AGH AgE Team
 */
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
            for(final ISimpleAgent agent : agents.values()) {
                try {
                    agent.finish();
                } catch(final ComponentException e) {
                    log.error("Finalization of {} resulted in an exception.", agent, e);
                }
            }
        });
        return super.finish();
    }

    /**
     * Executes the provided {@link Runnable} with locked agents' lock. The lock is locked before execution and
     * guaranteed to be unlocked after finishing the call.
     * <p>
     * <p>
     * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}.
     *
     * @param action the runnable to execute.
     * @see #withReadLock(Callable) for a rationale of behaviour for exceptions.
     * @see Locks#withReadLock(ReadWriteLock, Runnable)
     */
    protected final void withReadLock(final Runnable action) {
        assert (action != null);
        try {
            Locks.withReadLock(agentsLock, action);
        } catch(final Exception e) {
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
        if(temporaryAgentsList != null) {
            addAll(temporaryAgentsList);
            temporaryAgentsList = null;
        }
    }

    /**
     * Executes the provided {@link Callable} with locked agents' lock. The lock is locked before execution and
     * guaranteed to be unlocked after finishing the call.
     * <p>
     * <p>
     * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
     *
     * @param action the callable to execute.
     * @param <V>    a type of the value returned by the callable.
     * @return an object returned by the callable.
     * @see #withReadLock(Callable) for a rationale of behaviour for exceptions.
     * @see Locks#withWriteLock(ReadWriteLock, Callable)
     */
    @Nullable
    protected final <V> V withWriteLock(final Callable<V> action) {
        assert (action != null);
        try {
            return Locks.withWriteLock(agentsLock, action);
        } catch(final InterruptedException e) {
            log.warn("Interrupted.", e);
            Thread.currentThread().interrupt();
            return null;
        } catch(final Exception e) {
            log.error("An exception was thrown when having a lock.", e);
            throw Throwables.propagate(e);
        }
    }

    private boolean addAgentWithoutSynchronization(final ISimpleAgent agent) {
        assert (agent != null);
        try {
            final AgentAddress agentAddress = agent.getAddress();

            // Check whether another *instance* of agent exists - possible error?
            if(agents.containsKey(agentAddress) && agents.get(agentAddress) != agent) {
                log.error("Another instance of the agent with address '{}' already exists in this environment.",
                          agentAddress);
                return false;
            }
            agents.put(agentAddress, agent);
            agent.setAgentEnvironment(this);

            log.debug("Agent {} added to the aggregate {}.", agentAddress, getAddress());
            return true;
        } catch(final AgentException e) {
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
            if(agent != null) {
                agent.setAgentEnvironment(null);
                log.debug("Agent {} removed from the aggregate {}.", agent.getAddress(), getAddress());
            }
        } catch(final AgentException e) {
            log.error("Can't unset agent's environment.", e);
        }
        return true;
    }

    /**
     * Executes the provided {@link Callable} with locked agents' lock. The lock is locked before execution and
     * guaranteed to be unlocked after finishing the call.
     * <p>
     * <p>
     * This method locks on a <em>read</em> lock of the {@link ReadWriteLock}. Interrupted exception is logged and the
     * interrupted status is set. All other exceptions are logged and rethrown as runtime exceptions. (The rationale
     * behind this is that this method should be used only for locking all operations on agents' collection and it does
     * not throw exceptions.)
     *
     * @param action the callable to execute.
     * @param <V>    a type of the value returned by the callable.
     * @return an object returned by the callable.
     * @see Locks#withReadLock(ReadWriteLock, Callable)
     */
    @Nullable
    protected final <V> V withReadLock(final Callable<V> action) {
        assert (action != null);
        try {
            return Locks.withReadLock(agentsLock, action);
        } catch(final InterruptedException e) {
            log.warn("Interrupted.", e);
            Thread.currentThread().interrupt();
            return null;
        } catch(final Exception e) {
            log.error("An exception was thrown when having a lock.", e);
            throw Throwables.propagate(e);
        }
    }

    /**
     * Sets the agents list. Agents are stored in a temporary list and will be actually added during initialization.
     *
     * @param agents the new agents list
     */
    @PropertySetter(propertyName = "agents")
    public final void setAgents(final List<ISimpleAgent> agents) {
        temporaryAgentsList = agents;
        log.debug("Agents added.");
    }

    /**
     * Gets the agents list.
     *
     * @return the agents list
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * The returned iterator will be immutable and will be able to acces only the elements present at the time of this
     * call (there is no consistency with the underlying agents map).
     */
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
            for(final ISimpleAgent agent : agentsToAdd) {
                if(!addAgentWithoutSynchronization(agent)) {
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

    /**
     * Executes the provided {@link Runnable} with locked agents' lock. The lock is locked before execution and
     * guaranteed to be unlocked after finishing the call.
     * <p>
     * <p>
     * This method locks on a <em>write</em> lock of the {@link ReadWriteLock}.
     *
     * @param action the runnable to execute.
     * @see #withReadLock(Callable) for a rationale of behaviour for exceptions.
     * @see Locks#withWriteLock(ReadWriteLock, Runnable)
     */
    protected final void withWriteLock(final Runnable action) {
        assert (action != null);
        try {
            Locks.withWriteLock(agentsLock, action);
        } catch(final Exception e) {
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

    /**
     * Sends message to other agents on the same level in the tree of agents (or to the parent). To send message to
     * children use their {@link #deliverMessage(org.jage.communication.message.Message)} method.
     *
     * @param message The message to send.
     * @throws AgentException occurs when the message cannot be sent.
     */
    protected void sendMessage(final Message<AgentAddress, ?> message) {
        messagingService.sendMessage(message);
    }

    @Override
    public Collection<AgentAddress> validateAction(final SingleAction action) {
        return actionService.validateAction(action);
    }

	/* Services getters and setters */

    /**
     * Returns the query service of this aggregate.
     *
     * @return the query service of this aggregate.
     */
    public AggregateQueryService getQueryService() {
        return queryService;
    }

    /**
     * Package-private setter for tests.
     *
     * @param queryService a query service to use.
     */
    // XXX: This must be temporarily public because of tests.
    public void setQueryService(final AggregateQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Returns the action service of this aggregate.
     *
     * @return the action service of this aggregate.
     */
    public AggregateActionService getActionService() {
        return actionService;
    }

    /**
     * Package-private setter for tests.
     *
     * @param actionService an action service to use.
     */
    // XXX: This must be temporarily public because of tests.
    public void setActionService(final AggregateActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * Returns the messaging service of this aggregate.
     *
     * @return the messaging service of this aggregate.
     */
    public AggregateMessagingService getMessagingService() {
        return messagingService;
    }

    /**
     * Package-private setter for tests.
     *
     * @param messagingService a messaging service to use.
     */
    void setMessagingService(final AggregateMessagingService messagingService) {
        this.messagingService = messagingService;
    }

    /**
     * Provides agents lock. Returns always the same instance of the lock.
     *
     * @return agents lock
     */
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
