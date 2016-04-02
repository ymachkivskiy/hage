package org.hage.platform.component.runtime.event;

public final class CoreStoppingEvent extends BaseCoreComponentEvent {
    public CoreStoppingEvent() {
        super(CoreEventType.STOPPING);
    }
}
