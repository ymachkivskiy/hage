package org.hage.platform.component.execution.event;

public final class CorePausedEvent extends CoreComponentEvent {
    public CorePausedEvent() {
        super(CoreEventType.PAUSED);
    }
}
