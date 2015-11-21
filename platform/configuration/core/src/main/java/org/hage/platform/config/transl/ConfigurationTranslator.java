package org.hage.platform.config.transl;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.loader.Configuration;

import javax.annotation.Resource;

public class ConfigurationTranslator {

    private HabitatConfigurationTranslator translator;

    public ComputationConfiguration translate(Configuration configuration) {
        return ComputationConfiguration.builder()
                .localComponents(configuration.getLocalComponents())
                .globalComponents(configuration.getGlobalComponents())
                .habitatConfiguration(translator.toInternalModel(configuration.getHabitatConfiguration()))
                .build();
    }

    @Resource
    public void setTranslator(HabitatConfigurationTranslator translator) {
        this.translator = translator;
    }

}
