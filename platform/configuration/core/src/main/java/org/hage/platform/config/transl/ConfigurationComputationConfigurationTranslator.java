package org.hage.platform.config.transl;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.loader.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class ConfigurationComputationConfigurationTranslator {

    @Autowired
    private HabitatConfigurationTranslator translator;

    public ComputationConfiguration translate(Configuration configuration) {
        return ComputationConfiguration.builder()
                .localComponents(configuration.getLocalComponents())
                .globalComponents(configuration.getGlobalComponents())
                .habitatConfiguration(translator.toInternalModel(configuration.getHabitatConfiguration()))
                .build();
    }

}
