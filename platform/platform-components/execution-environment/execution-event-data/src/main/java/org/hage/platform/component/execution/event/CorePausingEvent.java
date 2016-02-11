package org.hage.platform.component.execution.event;

public final class CorePausingEvent extends BaseCoreComponentEvent {
    public CorePausingEvent() {
        super(CoreEventType.PAUSING);
    }
}
