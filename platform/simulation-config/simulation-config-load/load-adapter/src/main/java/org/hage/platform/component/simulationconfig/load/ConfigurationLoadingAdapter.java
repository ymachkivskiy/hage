package org.hage.platform.component.simulationconfig.load;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.ConfigurationDistributor;
import org.hage.platform.component.simulationconfig.event.ConfigurationLoadRequestEvent;
import org.hage.platform.component.simulationconfig.load.config.LoadingConfiguration;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.component.simulationconfig.load.generate.ComputationConfigurationGenerator;
import org.hage.platform.node.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@SingletonComponent
@Slf4j
class ConfigurationLoadingAdapter implements EventSubscriber {

    @Autowired
    private ConfigurationLoader configurationLoader;
    @Autowired
    private ComputationConfigurationGenerator configurationGenerator;
    @Autowired
    private ConfigurationDistributor configurationDistributor;
    @Autowired
    private LoadingConfiguration loadingConfiguration;

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationLoadRequest(ConfigurationLoadRequestEvent event) {
        log.debug("Configuration load request event: {}", event);

        if (loadingConfiguration.shouldLoadSimulationConfiguration()) {
            log.info("Will try to load simulation configuration");
            loadExternalConfiguration().ifPresent(this::notifyConfigurationLoaded);
        }
    }

    private Optional<InputConfiguration> loadExternalConfiguration() {
        try {
            log.info("Loading computation configuration.");
            // TODO: 05.04.17 temporary solution
            return ofNullable(configurationLoader.load());
        } catch (ConfigurationNotFoundException e) {
            log.warn("Configuration can not be loaded");
            return empty();
        }
    }

    private void notifyConfigurationLoaded(InputConfiguration inputConfiguration) {
        log.debug("Got input configuration {}", inputConfiguration);
        Configuration configuration = configurationGenerator.generate(inputConfiguration);

        log.debug("Notify configuration has been loaded {}", configuration);
        configurationDistributor.distributeUsingRatingConfiguration(configuration, inputConfiguration.getComputationRatingConfig());
    }

}
