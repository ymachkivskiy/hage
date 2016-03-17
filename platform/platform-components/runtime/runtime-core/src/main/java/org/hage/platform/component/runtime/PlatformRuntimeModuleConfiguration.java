package org.hage.platform.component.runtime;

import org.hage.platform.component.runtime.definition.Population;
import org.hage.platform.component.runtime.util.SimpleStatefulComponentsInitializer;
import org.hage.platform.component.runtime.util.StatefulComponentsInitializer;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformRuntimeModuleConfiguration.class)
class PlatformRuntimeModuleConfiguration {

    @Bean
    public ProportionsDivisor<Population> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public LocalExecutionUnitRepository executionUnitRepository() {
        return new LocalExecutionUnitRepository();
    }

    @Bean
    public StatefulComponentsInitializer statefulComponentsInitializer() {
        return new SimpleStatefulComponentsInitializer();
    }

}
