package org.hage;

import org.hage.platform.config.PlatformConfigurationHub;
import org.hage.platform.config.PlatformUtilCliModuleConfiguration;
import org.hage.platform.config.parse.Args4JParsingEngine;
import org.hage.platform.config.parse.ParsingEngine;
import org.hage.platform.util.connection.remote.config.RemoteConnectionConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(PlatformUtilCliModuleConfiguration.class)
public class CmdConfiguration {
//// TODO: temporary
    @Bean
    public PlatformConfigurationHub platformConfigurationHub() {
        return new PlatformConfigurationHub();
    }

    @Bean
    public ParsingEngine parsingEngine() {
        return new Args4JParsingEngine();
    }
    @Bean
    public RemoteConnectionConfigurationProvider remoteConnectionConfigurationProvider() {
        return new RemoteConnectionConfigurationProvider();
    }

}
