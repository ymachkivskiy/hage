package org.hage.platform.component.execution.event;

public final class CoreConfiguredEvent extends CoreComponentEvent {
    public CoreConfiguredEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
