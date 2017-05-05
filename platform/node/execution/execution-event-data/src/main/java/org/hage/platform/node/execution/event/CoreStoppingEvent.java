package org.hage.platform.node.execution.event;

public final class CoreStoppingEvent extends CoreComponentEvent {
    public CoreStoppingEvent() {
        super(CoreEventType.STOPPING);
    }
}
