package org.hage.platform.config.load;

import org.hage.platform.config.load.xml.XmlConfigurationLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformXmlBasedConfigProviderModuleConfiguration.class)
class PlatformXmlBasedConfigProviderModuleConfiguration {

    @Bean
    public ConfigurationLoader xmlConfigurationLoader() throws Exception {
        return new XmlConfigurationLoader();
    }

}
