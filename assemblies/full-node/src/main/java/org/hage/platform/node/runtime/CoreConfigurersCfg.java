package org.hage.platform.node.runtime;

import org.hage.platform.cluster.loadbalance.LoadBalanceCoreConfigurer;
import org.hage.platform.node.runtime.container.ContainerCoreConfigurer;
import org.hage.platform.node.runtime.populationinit.PopulationCoreConfigurer;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesCoreConfigurer;
import org.hage.platform.node.runtime.stopcondition.StopConditionCoreConfigurer;
import org.hage.platform.node.structure.connections.StructureCoreConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CoreConfigurersCfg {

    @Bean
    public ContainerCoreConfigurer containerCoreConfigurer() {
        return new ContainerCoreConfigurer(0);
    }

    @Bean
    public StructureCoreConfigurer structureCoreConfigurer() {
        return new StructureCoreConfigurer(1);
    }

    @Bean
    public UnitPropertiesCoreConfigurer unitPropertiesCoreConfigurer() {
        return new UnitPropertiesCoreConfigurer(2);
    }

    @Bean
    public StopConditionCoreConfigurer stopConditionCoreConfigurer() {
        return new StopConditionCoreConfigurer(3);
    }

    @Bean
    public PopulationCoreConfigurer populationCoreConfigurer() {
        return new PopulationCoreConfigurer(4);
    }

    @Bean
    public LoadBalanceCoreConfigurer loadBalanceCoreConfigurer() {
        return new LoadBalanceCoreConfigurer(5);
    }

}
