package org.hage.platform.component.lifecycle;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.CoreReadyEvent;
import org.hage.platform.component.lifecycle.event.ExitRequestedEvent;
import org.hage.platform.component.simulationconfig.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.AsynchronousLifecycleCommand.EXIT;
import static org.hage.platform.component.lifecycle.AsynchronousLifecycleCommand.START;
import static org.hage.platform.component.lifecycle.LifecycleEvent.CONFIGURE;

@SingletonComponent
@Slf4j
public class EventDrivenCommandInvoker implements EventSubscriber {

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationUpdated(ConfigurationUpdatedEvent event) {
        log.debug("Configuration updated event: {}.", event);
        lifecycleCommandInvoker.invokeCommand(stMachine -> stMachine.fire(CONFIGURE));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onCoreReady(CoreReadyEvent event) {
        log.debug("On core configured event: {}", event);
        lifecycleCommandInvoker.invokeCommand(START);
    }


    @Subscribe
    @SuppressWarnings("unused")
    public void onExitRequestedEvent(ExitRequestedEvent event) {
        log.debug("Exit requested by event: {}.", event);
        lifecycleCommandInvoker.invokeCommand(EXIT);
    }

}
