package org.hage.platform.component.execution.event;

public final class CoreReadyEvent extends CoreComponentEvent {
    public CoreReadyEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
