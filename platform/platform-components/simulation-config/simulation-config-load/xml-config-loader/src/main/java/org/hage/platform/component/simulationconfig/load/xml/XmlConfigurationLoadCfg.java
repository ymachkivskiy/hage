package org.hage.platform.component.simulationconfig.load.xml;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.component.simulationconfig.load.ConfigurationLoader;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
public class XmlConfigurationLoadCfg {

    @Bean
    public ConfigurationLoader xmlConfigurationLoader() {
        return new XmlConfigurationLoader();
    }

}
