package org.hage.platform.component.lifecycle.event;

public enum LifecycleEvent {
    /**
     * Sent by the bootstrapper.
     */
    INITIALIZE,
    /**
     * Sent by the configuration service when configuration is available.
     */
    CONFIGURE,
    /**
     * Starts the computation.
     */
    START_COMMAND,
    /**
     * Notifies that the core component is starting.
     */
    CORE_STARTING,
    /**
     * Pauses the computation.
     */
    PAUSE,
    /**
     * Resumes the computation.
     */
    RESUME,
    /**
     * Stops the computation completely.
     */
    STOP_COMMAND,
    /**
     * Notifies that the core component has stopped.
     */
    CORE_STOPPED,
    /**
     * Clears the node from the computation configuration.
     */
    CLEAR,
    /**
     * Indicates that an error occurred.
     */
    ERROR,
    /**
     * Terminates the node.
     */
    EXIT
}
