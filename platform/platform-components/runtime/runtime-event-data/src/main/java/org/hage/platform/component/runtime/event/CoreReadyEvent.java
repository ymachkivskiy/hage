package org.hage.platform.component.runtime.event;

public final class CoreReadyEvent extends BaseCoreComponentEvent {
    public CoreReadyEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
