package org.hage.platform.node.execution.event;

public final class CoreStartingEvent extends CoreComponentEvent {
    public CoreStartingEvent() {
        super(CoreEventType.STARTING);
    }
}
