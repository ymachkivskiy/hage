package org.hage.platform.config;

import org.hage.platform.config.loader.Configuration;

public class SimpleConfigurationTranslator implements ConfigurationTranslator {
    @Override
    public ComputationConfiguration translate(Configuration configuration) {
        return ComputationConfiguration.builder()
                .localComponents(configuration.getLocalComponents())
                .globalComponents(configuration.getGlobalComponents())
                .build();
    }
}
