package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.Action;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hage.platform.config.event.ConfigurationLoadRequestEvent.configurationLoadRequest;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InitializationAction implements Action {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute() {
        log.debug("Initializing LifecycleEngine.");

        notifyConfigurationCanBeLoaded();

        log.debug("Node has finished initialization.");
    }

    private void notifyConfigurationCanBeLoaded() {
        eventBus.post(configurationLoadRequest());
    }


}
