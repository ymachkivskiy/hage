package org.hage.platform.component.execution.event;

public final class CoreStartedEvent extends BaseCoreComponentEvent {
    public CoreStartedEvent() {
        super(CoreEventType.STARTED);
    }
}
