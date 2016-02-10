package org.hage.platform.component.agent;


import com.google.common.base.Objects;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.communication.message.Message;
import org.hage.platform.component.execution.IllegalOperationException;
import org.hage.platform.component.query.AgentEnvironmentQuery;
import org.hage.property.*;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Queues.newLinkedBlockingQueue;


/**
 * Abstract agent implementation.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractAgent extends ClassPropertyContainer implements IAgent {

    private static final long serialVersionUID = 3L;
    @Nonnull
    private final AgentAddress address;
    @Nonnull
    private final Queue<Message<AgentAddress, ?>> messages = newLinkedBlockingQueue();

    public AbstractAgent(@Nonnull final AgentAddressSupplier supplier) {
        this(supplier.get());
    }

    public AbstractAgent(@Nonnull final AgentAddress address) {
        this.address = checkNotNull(address);
    }

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }

    @Override
    public final void deliverMessage(@Nonnull final Message<AgentAddress, ?> message) {
        messages.add(message);
    }

    @Override
    @PropertyGetter(propertyName = Properties.ADDRESS)
    @Nonnull
    public final AgentAddress getAddress() {
        return address;
    }

    /**
     * Returns this agent's messages queue.
     *
     * @return this agent's messages queue
     */
    @Nonnull
    protected final Queue<Message<AgentAddress, ?>> getMessages() {
        return messages;
    }

    /**
     * Queries the local environment of this agent.
     * <p>
     * This is just an utility method that basically calls: {@code
     * query.execute(agentEnvironment)
     * } after verifying parameters.
     *
     * @param query The query to perform on the agent's local environment.
     * @return A result of the query.
     * @throws IllegalStateException if the environment is not available.
     */
    protected <E extends IAgent, T> Collection<T> queryEnvironment(final AgentEnvironmentQuery<E, T> query) {
        checkState(hasAgentEnvironment(), "Agent has no environment.");
        return query.execute(getAgentEnvironment());
    }

    /**
     * Checks if this agent has a local environment.
     *
     * @return <TT>true</TT> if this agent has a local environment; otherwise - <TT>false</TT>
     */
    protected final boolean hasAgentEnvironment() {
        return getAgentEnvironment() != null;
    }

    /**
     * Provides local environment. An agent don't have to be in an environment, because it is root agent or the
     * environment is not yet set.
     *
     * @return instance of environment or null
     */
    @CheckForNull
    protected abstract IAgentEnvironment getAgentEnvironment();

    /**
     * Queries the local environment of a parent of this agent.
     *
     * @param query The query to perform on the agent's parent's local environment.
     * @return A result of the query.
     * @throws IllegalStateException if the environment is not available.
     * @see IAgentEnvironment#queryParent(AgentEnvironmentQuery)
     */
    protected <E extends IAgent, T> Collection<T> queryParentEnvironment(final AgentEnvironmentQuery<E, T> query) {
        checkState(hasAgentEnvironment(), "Agent has no environment.");
        return getAgentEnvironment().queryParent(query);
    }

    /**
     * Returns the address of the parent of this agent.
     *
     * @return the address of the parent of this agent or null if parent is not available.
     */
    @CheckForNull
    protected AgentAddress getParentAddress() {
        if(hasAgentEnvironment()) {
            return getAgentEnvironment().getAddress();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method is overridden to implement virtual properties. If agent can't find property with given path it ask
     * about the property it's parent (through agent environment).
     */
    @Override
    @CheckForNull
    public final Property getProperty(final String name) throws InvalidPropertyPathException {
        Property result;
        try {
            result = super.getProperty(name);
        } catch(final InvalidPropertyPathException e) {
            try {
                if(getAgentEnvironment() instanceof IPropertyContainer) {
                    result = ((IPropertyContainer) getAgentEnvironment()).getProperty(name);
                } else {
                    throw new InvalidPropertyPathException("No path to property.");
                }
            } catch(final IllegalOperationException | InvalidPropertyPathException e2) {
                throw e;
            }
        }
        return result;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(address);
    }

    @Override
    public final boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof AbstractAgent)) {
            return false;
        }
        final AbstractAgent other = (AbstractAgent) obj;
        return Objects.equal(address, other.address);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(address).toString();
    }


    /**
     * AbstractAgent properties.
     *
     * @author AGH AgE Team
     */
    public static class Properties {

        /**
         * Agent address property.
         */
        public static final String ADDRESS = "address";
    }
}
