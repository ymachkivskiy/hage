package org.hage.platform.component.execution.event;

public final class CoreStoppingEvent extends BaseCoreComponentEvent {
    public CoreStoppingEvent() {
        super(CoreEventType.STOPPING);
    }
}
