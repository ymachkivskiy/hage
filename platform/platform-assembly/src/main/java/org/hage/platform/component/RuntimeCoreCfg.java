package org.hage.platform.component;

import org.hage.platform.component.runtime.execution.ExecutionCore;
import org.hage.platform.component.runtime.execution.NodeExecutionCore;
import org.hage.platform.component.runtime.execution.cycle.FixedPhaseOrderOrderClassifier;
import org.hage.platform.component.runtime.execution.cycle.PostStepPhaseOrderClassifier;
import org.hage.platform.component.runtime.execution.phase.ExecutionPhasesProvider;
import org.hage.platform.component.runtime.execution.phase.FixedExecutionPhasesProvider;
import org.hage.platform.component.runtime.init.BaseRuntimeInitializer;
import org.hage.platform.component.runtime.init.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.RuntimeInitializer;
import org.hage.platform.component.runtime.unit.NodeAgentUnitsRepo;
import org.hage.platform.component.runtime.util.SimpleStatefulPrototypeComponentsInitializer;
import org.hage.platform.component.runtime.util.StatefulPrototypeComponentsInitializer;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hage.platform.component.runtime.execution.ExecutionUnitPhase.AGENTS_STEP;
import static org.hage.platform.component.runtime.execution.PostStepPhase.STRUCTURE_UPDATE;

@Configuration
public class RuntimeCoreCfg {

    @Bean
    public ProportionsDivisor<Population> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public RuntimeInitializer runtimeInitializer() {
        return new BaseRuntimeInitializer();
    }

    @Bean
    public StatefulPrototypeComponentsInitializer statefulComponentsInitializer() {
        return new SimpleStatefulPrototypeComponentsInitializer();
    }

    @Bean
    public ExecutionPhasesProvider coreExecutorPhasesProvider() {
        return new FixedExecutionPhasesProvider(
            AGENTS_STEP
        );
    }

    @Bean
    public PostStepPhaseOrderClassifier postStepPhaseOrderClassifier() {
        return new FixedPhaseOrderOrderClassifier(
            STRUCTURE_UPDATE
        );
    }

    @Bean
    public ExecutionCore executionCore() {
        return new NodeExecutionCore();
    }

    @Bean
    public NodeAgentUnitsRepo nodeAgentUnitsRepo() {
        return new NodeAgentUnitsRepo();
    }

}
