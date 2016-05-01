package org.hage.platform.component.execution;

import org.hage.platform.component.execution.step.StepPhaseFactory;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationPerformingTask;
import org.hage.platform.component.runtime.migration.external.ExternalMigrationTaskFactory;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationPerformingTask;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationTaskFactory;
import org.hage.platform.component.runtime.stepphase.*;
import org.hage.platform.component.structure.stepphase.StructureChangeDistributionStepPhase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.hage.platform.component.execution.step.StaticFactoryBuilder.staticFactoryBuilder;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
class StepPhaseOrderCfg {

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private ClusterMembersViewPreparePhase clusterMembersViewPrepare;
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
    @Autowired
    private ExternalMigrationProcessPhase externalMigrationProcess;


    @Bean
    public StepPhaseFactory stepPhaseFactory() {
        return staticFactoryBuilder()
            .addNextIndependentPhases(
                internalMigrationsProcess,
                clusterMembersViewPrepare)
            .addNextIndependentPhases(synchForSubPhase("initial"))
            .addNextIndependentPhases(agentsStep)
            .addNextIndependentPhases(controlAgentStep)
            .addNextIndependentPhases(
                agentUnitPostProcess,
                structureChangeDistribution,
                externalMigrationProcess)
            .build();
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    SynchronizationStepPhase synchForSubPhase(String subPhase) {
        return new SynchronizationStepPhase(subPhase);
    }

    @Bean
    public InternalMigrationTaskFactory internalGroupMigrationPerformingTask() {
        return internalMigrationGroup -> beanFactory.getBean(InternalMigrationPerformingTask.class, internalMigrationGroup);
    }

    @Bean
    public ExternalMigrationTaskFactory externalGroupMigrationTaskFactory() {
        return externalMigrationGroup -> beanFactory.getBean(ExternalMigrationPerformingTask.class, externalMigrationGroup);
    }

}
