package org.hage.platform.component.execution.workplace;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.component.execution.agent.IAgent;
import org.hage.platform.component.execution.query.AgentEnvironmentQuery;
import org.hage.platform.component.execution.query.IQuery;
import org.hage.platform.component.execution.workplace.manager.WorkplaceManagerMessage;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


/**
 * Interface used by workplaces to communicate with the manager.
 *
 * @author AGH AgE Team
 */
public interface WorkplaceEnvironment {

    /**
     * Called when the workplace was stopped.
     *
     * @param workplace A workplace that stopped.
     */
    void onWorkplaceStop(@Nonnull Workplace workplace);

    /**
     * Queries workplaces located in this environment.
     *
     * @param query The query to perform.
     * @param <E>   A type of elements in the collection. E must be a realisation of {@link IAgent}.
     * @param <T>   A type of elements in the result.
     * @return the result of the query
     */
    @Nonnull
    <E extends IAgent, T> Collection<T> queryWorkplaces(AgentEnvironmentQuery<E, T> query);

    /**
     * Sends a message to other workplaces that are located in this environment.
     *
     * @param message The message to send.
     */
    void sendMessage(@Nonnull WorkplaceManagerMessage message);

    void requestRemoval(@Nonnull AgentAddress simpleWorkplace);

    /**
     * Returns addresses of all workplaces in the environment.
     *
     * @return the set of addresses of all workplaces.
     */
    @Nonnull
    Set<AgentAddress> getAddressesOfWorkplaces();

    void cacheQueryResults(@Nonnull AgentAddress address, @Nonnull IQuery<?, ?> query, Iterable<?> results);
}
