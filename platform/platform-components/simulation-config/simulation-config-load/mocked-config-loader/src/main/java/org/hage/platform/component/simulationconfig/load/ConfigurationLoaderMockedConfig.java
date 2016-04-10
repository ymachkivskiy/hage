package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@PlugableConfiguration
public class ConfigurationLoaderMockedConfig {

    @Bean
    public ConfigurationLoader configurationLoader() {
        return new MockedLoader();
    }

}
