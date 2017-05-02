package org.hage.platform.component.execution.event;

public final class CoreStoppedEvent extends CoreComponentEvent {
    public CoreStoppedEvent() {
        super(CoreEventType.STOPPED);
    }
}
