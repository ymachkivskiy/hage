package org.hage.platform.component.structure;

import org.hage.platform.component.structure.connections.ConfigurableStructure;
import org.hage.platform.component.structure.connections.grid.GridStructureCreationStrategy;
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
    public GridStructureCreationStrategy boxGridRepositoryCreationStrategy() {
        return new GridStructureCreationStrategy();
    }
    
    //endregion

}
