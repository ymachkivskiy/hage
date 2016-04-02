package org.hage.platform.component.runtime.event;

public final class CoreStoppedEvent extends BaseCoreComponentEvent {
    public CoreStoppedEvent() {
        super(CoreEventType.STOPPED);
    }
}
