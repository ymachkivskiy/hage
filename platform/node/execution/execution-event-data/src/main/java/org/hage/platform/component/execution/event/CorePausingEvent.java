package org.hage.platform.component.execution.event;

public final class CorePausingEvent extends CoreComponentEvent {
    public CorePausingEvent() {
        super(CoreEventType.PAUSING);
    }
}
