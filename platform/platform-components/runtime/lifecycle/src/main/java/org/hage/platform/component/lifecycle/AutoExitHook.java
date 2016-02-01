package org.hage.platform.component.lifecycle;


import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import org.hage.platform.component.lifecycle.event.LifecycleState;
import org.hage.platform.component.lifecycle.event.LifecycleStateChangedEvent;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


public final class AutoExitHook implements EventSubscriber, EventListener {

    private static final Logger log = LoggerFactory.getLogger(AutoExitHook.class);

    @Inject
    private EventBus eventBus;

    @Override
    public String toString() {
        return Objects.toStringHelper(this).toString();
    }

    @Override
    public EventListener getEventListener() {
        return this;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onLifecycleStateChangedEvent(LifecycleStateChangedEvent event) {

        log.debug("LifecycleStateChangedEvent: {}", event);

        if (LifecycleState.STOPPED.equals(event.getNewState())) {
            eventBus.post(new ExitRequestedEvent());
        }

    }

}
