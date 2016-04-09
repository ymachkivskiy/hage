package org.hage.platform.config;

import org.hage.platform.config.parse.Args4JParsingEngine;
import org.hage.platform.config.parse.CommonsCliParsingEngine;
import org.hage.platform.config.parse.ParsingEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformUtilCliModuleConfiguration {

    @Bean
    public PlatformConfigurationHub platformConfigurationHub() {
        return new PlatformConfigurationHub();
    }

    @Bean
    public ParsingEngine parsingEngine() {
        return new Args4JParsingEngine();
    }

}
