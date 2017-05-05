package org.hage.platform.node;

import org.hage.platform.node.structure.connections.ConfigurableStructure;
import org.hage.platform.node.structure.connections.grid.FullGridStructureCreationStrategy;
import org.hage.platform.node.structure.distribution.DistributedPositionsAddressingRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StructureCoreCfg {

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
    public DistributedPositionsAddressingRegistry distributedPositionsAddressingRegistry() {
            return new DistributedPositionsAddressingRegistry();
    }

}
