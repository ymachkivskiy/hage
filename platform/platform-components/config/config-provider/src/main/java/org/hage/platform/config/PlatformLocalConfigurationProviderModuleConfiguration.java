package org.hage.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformLocalConfigurationProviderModuleConfiguration {

    @Bean
    public ConfigurationStorageService computationConfigurationLocalProvider() {
        return new ConfigurationStorageService();
    }

}
