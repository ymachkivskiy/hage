package org.hage.platform.component.lifecycle;

public enum LifecycleEvent {

    PERFORM_CLUSTER_INITIALIZATION,

    CONFIGURE,

    START_SIMULATION,

    CORE_STARTING,

    PAUSE_FOR_RE_BALANCE,

    RESUME_SIMULATION,

    STOP_SIMULATION,

    CORE_STOPPED,

    CLEAR_SIMULATION_CONFIGURATION,

    ERROR,

    EXIT
}
