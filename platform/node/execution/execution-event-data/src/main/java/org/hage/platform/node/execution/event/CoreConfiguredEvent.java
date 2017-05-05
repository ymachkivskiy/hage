package org.hage.platform.node.execution.event;

public final class CoreConfiguredEvent extends CoreComponentEvent {
    public CoreConfiguredEvent() {
        super(CoreEventType.CONFIGURED);
    }
}
