package org.hage.configuration.service;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.bus.EventBus;
import org.hage.configuration.event.ConfigurationLoadRequestEvent;
import org.hage.configuration.event.ConfigurationLoadedEvent;
import org.hage.platform.argument.RuntimeArgumentsService;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.transl.ConfigurationComputationConfigurationTranslator;
import org.hage.platform.config.loader.Configuration;
import org.hage.platform.config.loader.ConfigurationSource;
import org.hage.platform.config.loader.IConfigurationLoader;
import org.hage.platform.config.xml.FileConfigurationSource;
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
    private ConfigurationComputationConfigurationTranslator configurationTranslator;
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
        ComputationConfiguration configuration = loadComputationConfiguration(configFilePath);
        notifyConfigurationLoaded(configuration);
    }

    private ComputationConfiguration loadComputationConfiguration(String configFilePath) {
        log.info("Loading computation configuration from {}.", configFilePath);

        try {
            return getComputationConfiguration(configFilePath);
        } catch (final ConfigurationException e) {
            log.error("Cannot load configuration from {}.", configFilePath, e);
            throw new ComponentException(e);
        }
    }

    private ComputationConfiguration getComputationConfiguration(String configFilePath) throws ConfigurationException {
        FileConfigurationSource configurationSource = new FileConfigurationSource(configFilePath);
        Configuration configuration = configurationLoader.loadConfiguration(configurationSource);
        return configurationTranslator.translate(configuration); //TODO wrapp  to some source
    }

    private void notifyConfigurationLoaded(ComputationConfiguration configuration) {
        log.info("Notify configuration has been loaded {}", configuration);

        eventBus.post(new ConfigurationLoadedEvent(configuration));
    }
}
