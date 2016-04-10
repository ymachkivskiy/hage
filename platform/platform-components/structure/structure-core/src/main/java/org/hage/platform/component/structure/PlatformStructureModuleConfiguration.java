package org.hage.platform.component.structure;

import org.hage.platform.component.structure.connections.ConfigurableStructure;
import org.hage.platform.component.structure.connections.grid.FullGridStructureCreationStrategy;
import org.hage.platform.component.structure.distribution.DistributedPositionsAddressingRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformStructureModuleConfiguration.class)
public class PlatformStructureModuleConfiguration {

    @Bean
    public ConfigurableStructure simulationStructure() {
        return new ConfigurableStructure();
    }

    //region repository creation strategies

    @Bean
    public FullGridStructureCreationStrategy fullGridRepositoryCreationStrategy() {
        return new FullGridStructureCreationStrategy();
    }
    
    //endregion

    @Bean
    public DistributedPositionsAddressingRegistry positionsRepo() {
            return new DistributedPositionsAddressingRegistry();
    }

}