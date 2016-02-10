package org.hage.platform.component.execution.event;

public final class CorePausedEvent extends BaseCoreComponentEvent {
    public CorePausedEvent() {
        super(CoreEventType.PAUSED);
    }
}
