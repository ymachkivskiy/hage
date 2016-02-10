package org.hage.platform.component.execution.event;

public final class CoreStoppedEvent extends BaseCoreComponentEvent {
    public CoreStoppedEvent() {
        super(CoreEventType.STOPPED);
    }
}
