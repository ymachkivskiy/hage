package org.hage.platform;

import org.hage.platform.component.simulationconfig.load.config.LoadConfigurationProvider;
import org.hage.platform.config.PlatformConfigurationHub;
import org.hage.platform.config.parse.Args4JParsingEngine;
import org.hage.platform.config.parse.ParsingEngine;
import org.hage.platform.util.connection.remote.config.RemoteConnectionConfigurationProvider;
import org.hage.platform.util.executors.config.external.ThreadingPolicyConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AppBaseCfg.class)
public class PlatformCliCfg {

    @Bean
    public PlatformConfigurationHub platformConfigurationHub() {
        return new PlatformConfigurationHub();
    }

    @Bean
    public ParsingEngine parsingEngine() {
        return new Args4JParsingEngine();
    }


    //region Configuration providers
    @Bean
    public RemoteConnectionConfigurationProvider remoteConnectionConfigurationProvider() {
        return new RemoteConnectionConfigurationProvider();
    }

    @Bean
    public LoadConfigurationProvider loadConfigurationProvider() {
        return new LoadConfigurationProvider();
    }

    @Bean
    public ThreadingPolicyConfigurationProvider threadingPolicyConfigurationProvider() {
        return new ThreadingPolicyConfigurationProvider();
    }

    //endregion
}
