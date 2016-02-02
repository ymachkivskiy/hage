package org.hage.platform.component.lifecycle;

public enum LifecycleState {
    /**
     * Initial state of the node.
     */
    OFFLINE,
    /**
     * Node has been initialized.
     */
    INITIALIZED,
    /**
     * Computation was loaded and initialized.
     */
    CONFIGURED,
    /**
     * Computation is running.
     */
    RUNNING,
    /**
     * Computation is paused.
     */
    PAUSED,
    /**
     * Computation has been stopped.
     */
    STOPPED,
    /**
     * Node has failed.
     */
    FAILED,
    /**
     * Node has terminated (terminal state).
     */
    TERMINATED
}
