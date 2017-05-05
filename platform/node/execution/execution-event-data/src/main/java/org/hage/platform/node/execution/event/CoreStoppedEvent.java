package org.hage.platform.node.execution.event;

public final class CoreStoppedEvent extends CoreComponentEvent {
    public CoreStoppedEvent() {
        super(CoreEventType.STOPPED);
    }
}
