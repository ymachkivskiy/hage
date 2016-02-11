package org.hage.platform.component.execution.event;

public final class CoreConfiguredEvent extends BaseCoreComponentEvent {
    public CoreConfiguredEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
