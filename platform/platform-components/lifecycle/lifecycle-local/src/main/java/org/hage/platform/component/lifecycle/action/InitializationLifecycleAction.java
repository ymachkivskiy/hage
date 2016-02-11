package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.config.event.ConfigurationLoadRequestEvent;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InitializationLifecycleAction implements LifecycleAction {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute() {
        log.debug("Initializing LifecycleEngine.");

        eventBus.post(new ConfigurationLoadRequestEvent());

        log.debug("Node has finished initialization.");
    }


}
