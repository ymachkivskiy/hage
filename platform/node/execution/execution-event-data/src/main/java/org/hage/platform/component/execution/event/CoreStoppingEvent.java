package org.hage.platform.component.execution.event;

public final class CoreStoppingEvent extends CoreComponentEvent {
    public CoreStoppingEvent() {
        super(CoreEventType.STOPPING);
    }
}
