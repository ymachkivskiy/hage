package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.springframework.context.annotation.Bean;

@PlugableConfiguration
public class ConfigurationLoaderMockedConfig {

    @Bean
    public ConfigurationLoader configurationLoader() {
        return new MockedLoader();
    }


}
