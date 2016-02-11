package org.hage.platform.component.execution.event;

public final class CoreStartingEvent extends BaseCoreComponentEvent {
    public CoreStartingEvent() {
        super(CoreEventType.STARTING);
    }
}
