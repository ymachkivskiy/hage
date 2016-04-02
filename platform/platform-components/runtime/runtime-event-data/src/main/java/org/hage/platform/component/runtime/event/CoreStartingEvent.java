package org.hage.platform.component.runtime.event;

public final class CoreStartingEvent extends BaseCoreComponentEvent {
    public CoreStartingEvent() {
        super(CoreEventType.STARTING);
    }
}
