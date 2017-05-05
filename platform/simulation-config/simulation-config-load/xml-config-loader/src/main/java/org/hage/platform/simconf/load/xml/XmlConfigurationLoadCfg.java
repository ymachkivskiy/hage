package org.hage.platform.simconf.load.xml;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.simconf.load.ConfigurationLoader;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
public class XmlConfigurationLoadCfg {

    @Bean
    public ConfigurationLoader xmlConfigurationLoader() {
        return new XmlConfigurationLoader();
    }

}
