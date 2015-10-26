package org.jage.configuration.service;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.jage.bus.EventBus;
import org.jage.configuration.event.ConfigurationLoadRequestEvent;
import org.jage.configuration.event.ConfigurationLoadedEvent;
import org.jage.platform.argument.RuntimeArgumentsService;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.config.ComputationConfiguration;
import org.jage.platform.config.loader.ConfigurationSource;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.FileConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


@ThreadSafe
@Slf4j
public class ConfigurationLoaderService implements IStatefulComponent {

    private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

    @Autowired
    private RuntimeArgumentsService argumentsService;
    @Autowired
    private IConfigurationLoader<ConfigurationSource> configurationLoader;
    @Autowired
    private EventBus eventBus;

    @Override
    public void init() {
        eventBus.register(this);
    }

    @Override
    public boolean finish() {
        eventBus.unregister(this);
        return true;
    }

    @Subscribe
    public void onConfigurationLoadRequest(@Nonnull final ConfigurationLoadRequestEvent event) {
        tryLoadAndPropagateConfiguration();
    }

    private void tryLoadAndPropagateConfiguration() {
        final String configFilePath = argumentsService.getCustomOption(COMPUTATION_CONFIGURATION);
        if (configFilePath != null) {
            loadAndPropagateConfiguration(configFilePath);
        } else {
            log.info("No computation configuration.");
        }
    }

    private void loadAndPropagateConfiguration(String configFilePath) {
        eventBus.post(new ConfigurationLoadedEvent(getConfiguration(configFilePath)));
    }

    private ComputationConfiguration getConfiguration(String configFilePath) {
        log.info("Loading computation configuration from {}.", configFilePath);

        try {
            return configurationLoader.loadConfiguration(new FileConfigurationSource(configFilePath));
        } catch (final ConfigurationException e) {
            log.error("Cannot load configuration from {}.", configFilePath, e);
            throw new ComponentException(e);
        }
    }
}
