package org.hage.platform.node.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.LifecycleAction;
import org.hage.platform.simconf.event.ConfigurationLoadRequestEvent;
import org.hage.platform.node.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@SingletonComponent
@RequiredArgsConstructor(access = PRIVATE)
public class InitializationLifecycleAction implements LifecycleAction {

    @Autowired
    private EventBus eventBus;

    @Override
    public void execute() {
        log.info("Initializing LifecycleEngine.");

        eventBus.post(new ConfigurationLoadRequestEvent());
    }


}
