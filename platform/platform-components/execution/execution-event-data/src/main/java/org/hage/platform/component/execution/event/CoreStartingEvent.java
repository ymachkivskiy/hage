package org.hage.platform.component.execution.event;

public final class CoreStartingEvent extends CoreComponentEvent {
    public CoreStartingEvent() {
        super(CoreEventType.STARTING);
    }
}
