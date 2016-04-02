package org.hage.platform.component.runtime.event;

public final class CoreStartedEvent extends BaseCoreComponentEvent {
    public CoreStartedEvent() {
        super(CoreEventType.STARTED);
    }
}
