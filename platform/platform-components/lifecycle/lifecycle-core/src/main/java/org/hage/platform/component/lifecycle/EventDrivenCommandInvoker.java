package org.hage.platform.component.lifecycle;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.event.CoreReadyEvent;
import org.hage.platform.component.runtime.event.CoreStartingEvent;
import org.hage.platform.component.runtime.event.CoreStoppedEvent;
import org.hage.platform.component.runtime.event.StopConditionFulfilledEvent;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.EXIT;
import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.*;
import static org.hage.platform.component.lifecycle.LifecycleEvent.*;

@Component
@Slf4j
public class EventDrivenCommandInvoker implements EventSubscriber {

    @Autowired
    private LifecycleEngine lifecycleEngine;

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
        log.debug("Configuration updated event: {}.", event);
        lifecycleEngine.performCommand(stMachine -> stMachine.fire(CONFIGURE));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreReady(CoreReadyEvent event) {
        log.debug("On core configured event: {}", event);
        lifecycleEngine.performCommand(START);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreStarting(CoreStartingEvent event) {
        log.debug("On core starting event: {}", event);
        lifecycleEngine.performCommand(lifecycleStateMachine -> lifecycleStateMachine.fire(CORE_STARTING));
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreStopped(CoreStoppedEvent event) {
        log.debug("On core stopped event: {}", event);
        lifecycleEngine.performCommand(lifecycleStateMachine -> lifecycleStateMachine.fire(CORE_STOPPED));
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onStopConditionFulfilledEvent(StopConditionFulfilledEvent event) {
        log.debug("Stop condition fulfilled event: {}.", event);
        lifecycleEngine.performCommand(STOP);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onExitRequestedEvent(ExitRequestedEvent event) {
        log.debug("Exit requested by event: {}.", event);
        lifecycleEngine.performCommand(EXIT);
    }

}
