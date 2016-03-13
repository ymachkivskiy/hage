package org.hage.platform.config.load;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationDistributor;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.event.ConfigurationLoadRequestEvent;
import org.hage.platform.config.load.definition.InputConfiguration;
import org.hage.platform.config.load.generate.ComputationConfigurationGenerator;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;


@Component
@Slf4j
class ConfigurationLoadingAdapter implements EventSubscriber {

    @Autowired
    private ConfigurationLoader configurationLoader;
    @Autowired
    private ComputationConfigurationGenerator configurationGenerator;
    @Autowired
    private ConfigurationDistributor configurationDistributor;

    @Subscribe
    @SuppressWarnings("unused")
    public void onConfigurationLoadRequest(ConfigurationLoadRequestEvent event) {
        log.debug("Configuration load request event: {}", event);

        loadConfiguration().ifPresent(this::notifyConfigurationLoaded);
    }

    private Optional<Configuration> loadConfiguration() {
        return loadExternalConfiguration().map(configurationGenerator::generate);
    }

    private Optional<InputConfiguration> loadExternalConfiguration() {
        try {
            log.info("Loading computation configuration.");
            return of(configurationLoader.load());
        } catch (ConfigurationNotFoundException e) {
            log.warn("Configuration can not be loaded");
            return empty();
        }
    }

    private void notifyConfigurationLoaded(Configuration configuration) {
        log.info("Notify configuration has been loaded {}", configuration);
        configurationDistributor.distribute(configuration);
    }

}
