package org.hage.platform.node.runtime;

import org.hage.platform.node.runtime.activepopulation.PopulationControllerInitialState;
import org.hage.platform.node.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.node.runtime.activepopulation.UnitActivePopulationControllerFactory;
import org.hage.platform.node.runtime.container.UnitComponentCreationController;
import org.hage.platform.node.runtime.container.UnitComponentCreationControllerFactory;
import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.node.runtime.location.UnitLocationController;
import org.hage.platform.node.runtime.location.UnitLocationControllerFactory;
import org.hage.platform.node.runtime.populationinit.GreedyPopulationDivisor;
import org.hage.platform.node.runtime.stateprops.PropertiesControllerInitialState;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesControllerFactory;
import org.hage.platform.node.runtime.unit.*;
import org.hage.platform.node.runtime.util.SimpleStatefulFinisher;
import org.hage.platform.node.runtime.util.StatefulFinisher;
import org.hage.platform.node.structure.Position;
import org.hage.util.proportion.Countable;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreConfigurersCfg.class)
public class RuntimeCoreCfg {

    @Bean
    public ProportionsDivisor<Population, Countable> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public StatefulFinisher statefulFinisher() {
        return new SimpleStatefulFinisher();
    }

    @Bean
    @Autowired
    public UnitComponentCreationControllerFactory unitComponentCreationControllerFactory(BeanFactory beanFactory) {
        return agentTargetEnvironment -> beanFactory.getBean(UnitComponentCreationController.class, agentTargetEnvironment);
    }

    @Autowired
    @Bean
    public UnitLocationControllerFactory unitLocationControllerFactory(BeanFactory beanFactory) {
        return position -> beanFactory.getBean(UnitLocationController.class, position);
    }

    @Autowired
    @Bean
    public UnitActivePopulationControllerFactory unitActivePopulationControllerFactory(BeanFactory beanFactory) {
        return new UnitActivePopulationControllerFactory() {

            @Override
            public UnitActivePopulationController createControllerWithExecutionEnvironment(AgentExecutionContextEnvironment agentEnvironment) {
                return beanFactory.getBean(UnitActivePopulationController.class, agentEnvironment);
            }

            @Override
            public UnitActivePopulationController createControllerWithExecutionEnvironmentAndInitialState(AgentExecutionContextEnvironment execEnv, PopulationControllerInitialState initialState) {
                return beanFactory.getBean(UnitActivePopulationController.class, execEnv, initialState);
            }

        };
    }

    @Autowired
    @Bean
    public AgentContextAdapterFactory agentContextAdapterFactory(BeanFactory beanFactory) {
        return (locationController, agentsCreator, agentsController, localUnitPropertiesProvider) -> beanFactory.getBean(AgentContextAdapter.class, locationController, agentsCreator, agentsController, localUnitPropertiesProvider);
    }

    @Autowired
    @Bean
    public UnitPropertiesControllerFactory unitPropertiesControllerFactory(BeanFactory beanFactory) {
        return new UnitPropertiesControllerFactory() {
            @Override
            public UnitPropertiesController createUnitPropertiesController(Position position) {
                return beanFactory.getBean(UnitPropertiesController.class, position);
            }

            @Override
            public UnitPropertiesController createPropertiesControllerWithInitialState(Position position, PropertiesControllerInitialState initialState) {
                return beanFactory.getBean(UnitPropertiesController.class, position, initialState);
            }
        };
    }

    @Autowired
    @Bean
    public UnitContainerFactory unitContainerFactory(BeanFactory beanFactory) {
        return () -> beanFactory.getBean(UnitContainer.class);
    }

}
