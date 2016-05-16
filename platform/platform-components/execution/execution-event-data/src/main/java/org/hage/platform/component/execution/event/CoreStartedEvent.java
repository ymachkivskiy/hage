package org.hage.platform.component.execution.event;

public final class CoreStartedEvent extends CoreComponentEvent {
    public CoreStartedEvent() {
        super(CoreEventType.STARTED);
    }
}
