package org.hage.platform.config.load;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockedLoaderModuleConfig {

    @Bean
    public ConfigurationLoader configurationLoader() {
        return new MockedLoader();
    }

}
