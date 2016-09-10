package org.hage.platform.component.lifecycle;

public enum LifecycleEvent {

    PERFORM_CLUSTER_INITIALIZATION,

    CONFIGURE,

    START_SIMULATION,

    PAUSE_FOR_RE_BALANCE,

    RESUME_SIMULATION,

    STOP_SIMULATION,

    ERROR,

    EXIT
}
