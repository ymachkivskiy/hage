package org.hage.platform.component.lifecycle;

import org.hage.platform.util.fsm.NotificationEventCreator;
import org.hage.services.core.LifecycleManager;

class LifecycleStateNotificationCreator implements NotificationEventCreator<LifecycleManager.State, LifecycleManager.Event, LifecycleStateChangedEvent> {

    @Override
    public LifecycleStateChangedEvent create(LifecycleManager.State previousState, LifecycleManager.Event event, LifecycleManager.State newState) {
        return new LifecycleStateChangedEvent(previousState, event, newState);
    }

}
