package org.hage.platform.component.lifecycle;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.event.CoreConfiguredEvent;
import org.hage.platform.component.simulationconfig.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.A_SYNC_START;
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
    public void onCoreReady(CoreConfiguredEvent event) {
        log.debug("On core configured event: {}", event);
        lifecycleCommandInvoker.invokeCommand(A_SYNC_START);
    }

}
