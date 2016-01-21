package org.hage.platform.config.local.provider;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.local.gen.ConfigurationComputationConfigurationTranslator;
import org.hage.platform.config.provider.Configuration;
import org.hage.platform.config.provider.ConfigurationProvider;
import org.hage.platform.config.provider.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
public abstract class BaseComputationConfigurationProvider implements ComputationConfigurationProvider {

    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private ConfigurationComputationConfigurationTranslator configurationTranslator;


    @Override
    public Optional<ComputationConfiguration> tryGetConfiguration() {

        Optional<ConfigurationSource> configurationSource = getConfigurationSource();

        if (configurationSource.isPresent()) {
            return loadConfiguration(configurationSource.get());
        } else {
            log.info("No computation configuration.");
            return empty();
        }

    }

    private Optional<ComputationConfiguration> loadConfiguration(ConfigurationSource configurationSource) {
        return loadExternalConfiguration(configurationSource)
            .map(configurationTranslator::translate);
    }

    private Optional<Configuration> loadExternalConfiguration(ConfigurationSource configSource) {
        log.info("Loading computation configuration from {}.", configSource);

        try {
            return of(configurationProvider.getConfiguration(configSource));
        } catch (final ConfigurationException e) {
            log.error("Cannot load configuration from {}.", configSource, e);
        }

        return empty();
    }

    protected abstract Optional<ConfigurationSource> getConfigurationSource();

}
