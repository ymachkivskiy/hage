package org.hage.platform.component;

import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.execution.ExecutionCore;
import org.hage.platform.component.runtime.execution.NodeExecutionCore;
import org.hage.platform.component.runtime.execution.cycle.FixedPhaseOrderOrderClassifier;
import org.hage.platform.component.runtime.execution.cycle.PostStepPhaseOrderClassifier;
import org.hage.platform.component.runtime.execution.phase.ExecutionPhasesProvider;
import org.hage.platform.component.runtime.execution.phase.FixedExecutionPhasesProvider;
import org.hage.platform.component.runtime.global.BasePopulationInitializer;
import org.hage.platform.component.runtime.global.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.unit.AgentsUnitFactory;
import org.hage.platform.component.runtime.unit.LocalAgentUnitsController;
import org.hage.platform.component.runtime.unit.context.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.component.runtime.util.SimpleStatefulInitializerAndFinisher;
import org.hage.platform.component.runtime.util.StatefulInitializer;
import org.hage.platform.component.structure.Position;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hage.platform.component.runtime.execution.ExecutionUnitPhase.AGENTS_STEP;
import static org.hage.platform.component.runtime.execution.ExecutionUnitPhase.CLEANUP;
import static org.hage.platform.component.runtime.execution.ExecutionUnitPhase.CONTROL_AGENT_STEP;
import static org.hage.platform.component.runtime.execution.PostStepPhase.CLEAN_CACHE;
import static org.hage.platform.component.runtime.execution.PostStepPhase.STRUCTURE_UPDATE;

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
    public SimpleStatefulInitializerAndFinisher statefulComponentsInitializer() {
        return new SimpleStatefulInitializerAndFinisher();
    }

    @Bean
    public ExecutionPhasesProvider coreExecutorPhasesProvider() {
        return new FixedExecutionPhasesProvider(
            CONTROL_AGENT_STEP,
            AGENTS_STEP,
            CLEANUP
        );
    }

    @Bean
    public PostStepPhaseOrderClassifier postStepPhaseOrderClassifier() {
        return new FixedPhaseOrderOrderClassifier(
            CLEAN_CACHE,
            STRUCTURE_UPDATE
        );
    }

    @Bean
    public ExecutionCore executionCore() {
        return new NodeExecutionCore();
    }

    @Bean
    public LocalAgentUnitsController nodeAgentUnitsRepo() {
        return new LocalAgentUnitsController();
    }

    @Bean
    @Autowired
    public AgentsUnitFactory agentsUnitFactory(final MutableInstanceContainer instanceContainer) {

        return new AgentsUnitFactory() {

            @Override
            protected UnitAgentCreationContext createUnitPopulationContext(UnitPopulationController unitPopulationController) {
                return beanFactory.getBean(UnitAgentCreationContext.class, instanceContainer.newChildContainer(), unitPopulationController);
            }

            @Override
            protected UnitLocationContext createUnitLocationContext(Position position) {
                return beanFactory.getBean(UnitLocationContext.class, position);
            }

            @Override
            protected UnitPopulationController createUnitPopulationController(AgentLocalEnvironment agentEnvironment) {
                return beanFactory.getBean(UnitPopulationController.class, agentEnvironment);
            }

        };

    }

}
