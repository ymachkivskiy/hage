package org.hage.platform.component.execution.workplace;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleState;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.component.lifecycle.event.LifecycleStateChangedEvent;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventSubscriber;

import javax.inject.Inject;


@Slf4j
public final class AutoExitHook implements EventSubscriber {

    @Inject
    private EventBus eventBus;

    @Subscribe
    @SuppressWarnings("unused")
    public void onLifecycleStateChangedEvent(LifecycleStateChangedEvent event) {

        log.debug("LifecycleStateChangedEvent: {}", event);

        if (LifecycleState.STOPPED.equals(event.getNewState())) {
            eventBus.post(new ExitRequestedEvent());
        }

    }

}
