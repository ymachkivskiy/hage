package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationConsumer;
import org.hage.platform.component.config.ConfigurationProvider;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private ConfigurationConsumer configurationConsumer;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        Configuration configuration = configurationProvider.provideConfiguration();

        configurationConsumer.acceptConfiguration(configuration);

        log.info("Node is configured.");
    }


}
