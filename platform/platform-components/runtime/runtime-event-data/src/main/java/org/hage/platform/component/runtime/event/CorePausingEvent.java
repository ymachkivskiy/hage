package org.hage.platform.component.runtime.event;

public final class CorePausingEvent extends BaseCoreComponentEvent {
    public CorePausingEvent() {
        super(CoreEventType.PAUSING);
    }
}
