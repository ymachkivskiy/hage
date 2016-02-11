package org.hage.platform.component.lifecycle.action;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.ComputationConfigurable;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.ConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private ComputationConfigurable computationConfigurable;

    @Autowired
    private ConfigurationProvider configurationStorageService;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        ComputationConfiguration configuration = configurationStorageService.provideConfiguration();

        computationConfigurable.configureUsing(configuration);

        log.info("Node is configured.");
    }


}
