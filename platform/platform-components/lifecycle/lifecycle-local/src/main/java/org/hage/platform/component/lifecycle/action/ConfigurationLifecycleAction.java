package org.hage.platform.component.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.lifecycle.LifecycleAction;
import org.hage.platform.component.simulation.structure.ComputationStructureConfigurator;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.ConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private ComputationStructureConfigurator computationStructureConfigurator;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        ComputationConfiguration configuration = configurationProvider.provideConfiguration();

        computationStructureConfigurator.configureWith(configuration);

        log.info("Node is configured.");
    }


}
