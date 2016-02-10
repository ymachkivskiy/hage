package org.hage.platform.component.lifecycle;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.event.CoreConfiguredEvent;
import org.hage.platform.component.execution.event.CoreStartingEvent;
import org.hage.platform.component.execution.event.CoreStoppedEvent;
import org.hage.platform.component.execution.event.StopConditionFulfilledEvent;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.*;

@Component
@Slf4j
public class EventDrivenCommandInvoker implements EventSubscriber, EventListener {

    @Autowired
    private LifecycleEngine lifecycleEngine;

    @Override
    public EventListener getEventListener() {
        return this;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
        log.debug("Configuration updated event: {}.", event);
        lifecycleEngine.performCommand(stMachine -> stMachine.fire(LifecycleEvent.CONFIGURE));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreConfigured(CoreConfiguredEvent event) {
        log.debug("On core configured event: {}", event);
        lifecycleEngine.performCommand(START);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreStarting(CoreStartingEvent event) {
        log.debug("On core starting event: {}", event);
        lifecycleEngine.performCommand(lifecycleStateMachine -> lifecycleStateMachine.fire(LifecycleEvent.CORE_STARTING));
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreStopped(CoreStoppedEvent event) {
        log.debug("On core stopped event: {}", event);
        lifecycleEngine.performCommand(lifecycleStateMachine -> lifecycleStateMachine.fire(LifecycleEvent.CORE_STOPPED));
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
