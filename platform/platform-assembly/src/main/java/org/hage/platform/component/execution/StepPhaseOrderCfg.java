package org.hage.platform.component.execution;

import org.hage.platform.component.execution.step.StepPhaseFactory;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationPerformingTask;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationTaskFactory;
import org.hage.platform.component.runtime.stepphase.*;
import org.hage.platform.component.structure.stepphase.StructureChangeDistributionStepPhase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hage.platform.component.execution.step.StaticFactoryBuilder.staticFactoryBuilder;

@Configuration
class StepPhaseOrderCfg {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private InternalMigrationProcessPhase internalMigrationsProcess;
    @Autowired
    private AgentsStepPhase agentsStep;
    @Autowired
    private ControlAgentStepPhase controlAgentStep;
    @Autowired
    private AgentUnitPostProcessPhase agentUnitPostProcess;
    @Autowired
    private StructureChangeDistributionStepPhase structureChangeDistribution;



    @Bean
    public StepPhaseFactory stepPhaseFactory() {
        return staticFactoryBuilder()
            .addNextIndependentPhases(internalMigrationsProcess)
            .addNextIndependentPhases(synchronizationForSubPhase("initial"))
            .addNextIndependentPhases(agentsStep)
            .addNextIndependentPhases(controlAgentStep)
            .addNextIndependentPhases(agentUnitPostProcess, structureChangeDistribution)
            .build();
    }

    private SynchronizationStepPhase synchronizationForSubPhase(String subPhase) {
        return beanFactory.getBean(SynchronizationStepPhase.class, subPhase);
    }

    @Bean
    public InternalMigrationTaskFactory groupMigrationPerformingTask() {
        return internalMigrationGroup -> beanFactory.getBean(InternalMigrationPerformingTask.class, internalMigrationGroup);
    }

}