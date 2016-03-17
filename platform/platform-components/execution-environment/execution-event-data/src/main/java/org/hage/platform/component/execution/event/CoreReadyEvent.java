package org.hage.platform.component.execution.event;

public final class CoreReadyEvent extends BaseCoreComponentEvent {
    public CoreReadyEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
