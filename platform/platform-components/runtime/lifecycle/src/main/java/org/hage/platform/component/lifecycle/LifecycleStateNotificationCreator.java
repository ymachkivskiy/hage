package org.hage.platform.component.lifecycle;

import org.hage.platform.component.services.core.LifecycleManager;
import org.hage.platform.util.fsm.NotificationEventCreator;

class LifecycleStateNotificationCreator implements NotificationEventCreator<LifecycleManager.State, LifecycleManager.Event, LifecycleStateChangedEvent> {

    @Override
    public LifecycleStateChangedEvent create(LifecycleManager.State previousState, LifecycleManager.Event event, LifecycleManager.State newState) {
        return new LifecycleStateChangedEvent(previousState, event, newState);
    }

}
