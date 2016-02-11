package org.hage.platform.config.load.adapter;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.distribution.ConfigurationDistributor;
import org.hage.platform.config.event.ConfigurationLoadRequestEvent;
import org.hage.platform.config.load.ConfigurationLoader;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.adapter.generate.ComputationConfigurationGenerator;
import org.hage.platform.config.load.definition.Configuration;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;


@Component
@Slf4j
public class ConfigurationLoadingAdapter implements EventSubscriber, EventListener {

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

    private Optional<ComputationConfiguration> loadConfiguration() {
        return loadExternalConfiguration().map(configurationGenerator::generate);
    }

    private Optional<Configuration> loadExternalConfiguration() {
        try {
            log.info("Loading computation configuration.");
            return of(configurationLoader.load());
        } catch (ConfigurationNotFoundException e) {
            log.warn("Configuration can not be loaded");
            return empty();
        }
    }

    private void notifyConfigurationLoaded(ComputationConfiguration configuration) {
        log.info("Notify configuration has been loaded {}", configuration);
        configurationDistributor.distribute(configuration);
    }


    @Override
    public EventListener getEventListener() {
        return this;
    }

}
