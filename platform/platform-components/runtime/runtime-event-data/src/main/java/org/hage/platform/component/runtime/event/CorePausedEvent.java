package org.hage.platform.component.runtime.event;

public final class CorePausedEvent extends BaseCoreComponentEvent {
    public CorePausedEvent() {
        super(CoreEventType.PAUSED);
    }
}
