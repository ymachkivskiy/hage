package org.hage.platform;

import org.hage.platform.component.simulationconfig.load.*;
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


    @Bean
    public Config2a_LightNotRepNotMigrAgentsConfigSupplier lightNotReproducibleNotMigratingAgent() {
        return new Config2a_LightNotRepNotMigrAgentsConfigSupplier();
    }


    @Bean
    public Config2b_LightReprNotMigrAgentsConfigSupplier config2b_lightReprNotMigrAgentsConfigSupplier() {
        return new Config2b_LightReprNotMigrAgentsConfigSupplier();
    }

    @Bean
    public Config1b_LightNotRepNotMigrAgentsConfigSupplier config1b_lightNotRepNotMigrAgentsConfigSupplier() {
        return new Config1b_LightNotRepNotMigrAgentsConfigSupplier();
    }

    @Bean
    public Config1c_LightNotRepNotMigrAgentsConfigSupplier config1c_lightNotRepNotMigrAgentsConfigSupplier() {
        return new Config1c_LightNotRepNotMigrAgentsConfigSupplier();
    }

    @Bean
    public Config1a_LoadBalancedMigrAgents config1a_loadBalancedMigrAgents() {
        return new Config1a_LoadBalancedMigrAgents();
    }

    @Bean
    public Config1a_NoLoadBalancedMigrAgents config1a_noLoadBalancedMigrAgents() {
        return new Config1a_NoLoadBalancedMigrAgents();
    }

    @Bean
    public SimulationConfigChooseConfigurationProvider simulationConfigChooseConfigurationProvider() {
        return new SimulationConfigChooseConfigurationProvider();
    }

    @Bean
    public SimulationChooseConfigItem simulationChooseConfigItem() {
        return new SimulationChooseConfigItem();
    }
}
