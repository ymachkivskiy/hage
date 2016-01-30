package org.hage.platform.config;

import org.hage.platform.config.provider.ConfigurationProvider;
import org.hage.platform.config.xml.XmlConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformXmlBasedConfigProviderModuleConfiguration {

    @Bean
    public ConfigurationProvider xmlConfigurationProvider() throws Exception {
        return new XmlConfigurationProvider();
    }

    @Bean
    public ConfigurationConversionService configurationConversionService() {
        return new ConfigurationConversionService();
    }

}
