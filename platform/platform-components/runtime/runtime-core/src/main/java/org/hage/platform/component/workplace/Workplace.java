package org.hage.platform.component.workplace;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.message.Message;
import org.hage.platform.component.agent.IAgent;
import org.hage.platform.component.agent.IAgentEnvironment;
import org.hage.platform.component.execution.WorkplaceException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;


/**
 * The component that carries out simulation. It is responsible for agents, their life and activities.
 *
 * @author AGH AgE Team
 */
public interface Workplace extends IAgentEnvironment {

    /**
     * Sets new workplace environment to this workplace.
     *
     * @param workplaceEnvironment A workplace environment to set.
     * @throws WorkplaceException when environment is already set.
     */
    void setEnvironment(@CheckForNull WorkplaceEnvironment workplaceEnvironment);

    /**
     * Starts the workplace.
     */
    void start();

    /**
     * Pauses the workplace.
     */
    void pause();

    /**
     * Resumes the workplace if paused.
     */
    void resume();

    /**
     * Stops the workplace. After calling this method workplace can be restarted - using the {@link #start} method.
     */
    void stop();

    /**
     * Checks whether the agent is running.
     *
     * @return true, if the agent is running.
     */
    boolean isRunning();

    /**
     * Checks whether the agent is paused.
     *
     * @return true, if the agent is paused.
     */
    boolean isPaused();

    /**
     * Checks whether the agent is stopped.
     *
     * @return true, if the agent is stopped.
     */
    boolean isStopped();

    /**
     * Delivers an agent message to the workplace.
     *
     * @param message A message to be delivered.
     */
    void deliverMessage(@Nonnull Message<AgentAddress, ?> message);

    void sendAgent(@Nonnull IAgent agent);

    /**
     * Returns the agent operating in this workplace.
     *
     * @return an agent.
     */
    @Nonnull
    IAgent getAgent();

    /**
     * States of workplaces.
     */
    enum State {
        /**
         * A workplace is running and performing computation.
         */
        RUNNING,
        /**
         * A workplace is stopping - it will transition into STOPPED state.
         */
        STOPPING,
        /**
         * A workplace is paused temporarily.
         */
        PAUSED,
        /**
         * The workplace is stopped.
         */
        STOPPED
    }
}
