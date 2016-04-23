package org.hage.platform.component;

import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.global.BasePopulationInitializer;
import org.hage.platform.component.runtime.global.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.unit.AgentsUnitFactory;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitActivePopulationController;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.util.SimpleStatefulFinisher;
import org.hage.platform.component.runtime.util.StatefulFinisher;
import org.hage.platform.component.structure.Position;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RuntimeCoreCfg {

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    public ProportionsDivisor<Population> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public PopulationInitializer runtimeInitializer() {
        return new BasePopulationInitializer();
    }

    @Bean
    public StatefulFinisher statefulFinisher() {
        return new SimpleStatefulFinisher();
    }

    @Bean
    @Autowired
    public AgentsUnitFactory agentsUnitFactory(final MutableInstanceContainer instanceContainer) {

        return new AgentsUnitFactory() {

            @Override
            protected UnitAgentCreationContext createUnitPopulationContext(UnitActivePopulationController unitActivePopulationController) {
                return beanFactory.getBean(UnitAgentCreationContext.class, instanceContainer.newChildContainer(), unitActivePopulationController);
            }

            @Override
            protected UnitLocationContext createUnitLocationContext(Position position) {
                return beanFactory.getBean(UnitLocationContext.class, position);
            }

            @Override
            protected UnitActivePopulationController createUnitPopulationController(AgentLocalEnvironment agentEnvironment) {
                return beanFactory.getBean(UnitActivePopulationController.class, agentEnvironment);
            }

        };

    }

}
