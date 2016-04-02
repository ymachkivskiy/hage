package org.hage.platform.component.structure;

import org.hage.platform.component.structure.connections.ConfigurableStructure;
import org.hage.platform.component.structure.connections.grid.FullGridStructureCreationStrategy;
import org.hage.platform.component.structure.distribution.DummyLocalPositionsRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformStructureModuleConfiguration.class)
public class PlatformStructureModuleConfiguration {

    @Bean
    public ConfigurableStructure gridConnectionsRepository() {
        return new ConfigurableStructure();
    }

    //region repository creation strategies

    @Bean
    public FullGridStructureCreationStrategy boxGridRepositoryCreationStrategy() {
        return new FullGridStructureCreationStrategy();
    }
    
    //endregion

    @Bean
    public DummyLocalPositionsRepo positionsRepo() {
            return new DummyLocalPositionsRepo();
    }

}
