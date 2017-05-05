package org.hage.platform.node.execution.event;

public final class CorePausingEvent extends CoreComponentEvent {
    public CorePausingEvent() {
        super(CoreEventType.PAUSING);
    }
}
