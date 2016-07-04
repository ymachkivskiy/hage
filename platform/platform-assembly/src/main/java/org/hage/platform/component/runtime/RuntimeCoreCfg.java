package org.hage.platform.component.runtime;

import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationControllerFactory;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.container.UnitComponentCreationControllerFactory;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.location.UnitLocationControllerFactory;
import org.hage.platform.component.runtime.populationinit.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesControllerFactory;
import org.hage.platform.component.runtime.unit.AgentContextAdapter;
import org.hage.platform.component.runtime.unit.AgentContextAdapterFactory;
import org.hage.platform.component.runtime.util.SimpleStatefulFinisher;
import org.hage.platform.component.runtime.util.StatefulFinisher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreConfigurersCfg.class)
public class RuntimeCoreCfg {

    @Bean
    public GreedyPopulationDivisor populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public StatefulFinisher statefulFinisher() {
        return new SimpleStatefulFinisher();
    }

    @Bean
    @Autowired
    public UnitComponentCreationControllerFactory unitComponentCreationControllerFactory(MutableInstanceContainer instanceContainer, BeanFactory beanFactory) {
        return new UnitComponentCreationControllerFactory() {
            @Override
            public UnitComponentCreationController createControllerWithTargetEnv(AgentsTargetEnvironment agentTargetEnvironment) {
                return beanFactory.getBean(UnitComponentCreationController.class, instanceContainer.newChildContainer(), agentTargetEnvironment);
            }
        };
    }

    @Autowired
    @Bean
    public UnitLocationControllerFactory unitLocationControllerFactory(BeanFactory beanFactory) {
        return position -> beanFactory.getBean(UnitLocationController.class, position);
    }

    @Autowired
    @Bean
    public UnitActivePopulationControllerFactory unitActivePopulationControllerFactory(BeanFactory beanFactory) {
        return agentEnvironment -> beanFactory.getBean(UnitActivePopulationController.class, agentEnvironment);
    }

    @Autowired
    @Bean
    public AgentContextAdapterFactory agentContextAdapterFactory(BeanFactory beanFactory) {
        return (locationController, agentsCreator, agentsController, localUnitPropertiesProvider) -> beanFactory.getBean(AgentContextAdapter.class, locationController, agentsCreator, agentsController, localUnitPropertiesProvider);
    }

    @Autowired
    @Bean
    public UnitPropertiesControllerFactory unitPropertiesControllerFactory(BeanFactory beanFactory) {
        return (position, componentCreationController) -> beanFactory.getBean(UnitPropertiesController.class, position, componentCreationController);
    }

}
