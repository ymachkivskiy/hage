package org.hage.platform.component.structure;

import org.hage.platform.component.structure.grid.ConnectionRepositoryHolder;
import org.hage.platform.component.structure.grid.GridConnectionsConfigurator;
import org.hage.platform.component.structure.grid.RepositoryCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformStructureModuleConfiguration.class)
public class PlatformStructureModuleConfiguration {

    @Bean
    public ConnectionRepositoryHolder gridConnectionsRepository() {
        return new ConnectionRepositoryHolder();
    }

    @Bean
    public GridConnectionsConfigurator gridConnectionsConfigurator() {
        return new RepositoryCreator();
    }

}
