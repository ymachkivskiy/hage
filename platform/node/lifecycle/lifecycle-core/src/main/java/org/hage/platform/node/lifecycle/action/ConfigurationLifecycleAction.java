package org.hage.platform.node.lifecycle.action;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.LifecycleAction;
import org.hage.platform.simconf.Configuration;
import org.hage.platform.simconf.ConfigurationProvider;
import org.hage.platform.simconf.SimulationConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;

@SingletonComponent
@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConfigurationLifecycleAction implements LifecycleAction {

    @Autowired
    private SimulationConfigurator configurationConsumer;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void execute() {
        log.info("Configuring the computation.");

        Configuration configuration = configurationProvider.provideConfiguration();

        configurationConsumer.configureSimulation(configuration);

        log.info("Node is configured.");
    }


}
