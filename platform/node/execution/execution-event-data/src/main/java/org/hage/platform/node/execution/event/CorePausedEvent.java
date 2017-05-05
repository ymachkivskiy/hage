package org.hage.platform.node.execution.event;

public final class CorePausedEvent extends CoreComponentEvent {
    public CorePausedEvent() {
        super(CoreEventType.PAUSED);
    }
}
