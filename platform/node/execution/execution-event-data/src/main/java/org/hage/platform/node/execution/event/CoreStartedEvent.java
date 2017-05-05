package org.hage.platform.node.execution.event;

public final class CoreStartedEvent extends CoreComponentEvent {
    public CoreStartedEvent() {
        super(CoreEventType.STARTED);
    }
}
