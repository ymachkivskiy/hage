package org.hage.platform.component.runtime;

import org.hage.platform.component.loadbalance.LoadBalanceCoreConfigurer;
import org.hage.platform.component.runtime.container.ContainerCoreConfigurer;
import org.hage.platform.component.runtime.populationinit.PopulationCoreConfigurer;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesCoreConfigurer;
import org.hage.platform.component.runtime.stopcondition.StopConditionCoreConfigurer;
import org.hage.platform.component.structure.connections.StructureCoreConfigurer;
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
    public PopulationCoreConfigurer populationCoreConfigurer() {
        return new PopulationCoreConfigurer(2);
    }

    @Bean
    public StopConditionCoreConfigurer stopConditionCoreConfigurer() {
        return new StopConditionCoreConfigurer(3);
    }

    @Bean
    public UnitPropertiesCoreConfigurer unitPropertiesCoreConfigurer() {
        return new UnitPropertiesCoreConfigurer(4);
    }

    @Bean
    public LoadBalanceCoreConfigurer loadBalanceCoreConfigurer() {
        return new LoadBalanceCoreConfigurer(5);
    }

}
