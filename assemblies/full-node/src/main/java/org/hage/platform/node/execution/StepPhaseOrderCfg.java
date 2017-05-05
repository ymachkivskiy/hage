package org.hage.platform.node.execution;

import org.hage.platform.node.execution.phase.ExecutionPhaseFactory;
import org.hage.platform.node.execution.phase.OrderedPhasesFactory;
import org.hage.platform.node.runtime.migration.external.ExternalMigrationPerformingTask;
import org.hage.platform.node.runtime.migration.external.ExternalMigrationTaskFactory;
import org.hage.platform.node.runtime.migration.internal.InternalMigrationPerformingTask;
import org.hage.platform.node.runtime.migration.internal.InternalMigrationTaskFactory;
import org.hage.platform.node.runtime.unitmove.UnitUnpackTask;
import org.hage.platform.node.runtime.unitmove.UnitUnpackTaskFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;
import static org.hage.platform.node.execution.phase.ExecutionPhaseType.*;

@Configuration
class StepPhaseOrderCfg {

    @Bean
    @Autowired
    public InternalMigrationTaskFactory internalGroupMigrationPerformingTask(BeanFactory beanFactory) {
        return internalMigrationGroup -> beanFactory.getBean(InternalMigrationPerformingTask.class, internalMigrationGroup);
    }

    @Bean
    @Autowired
    public ExternalMigrationTaskFactory externalGroupMigrationTaskFactory(BeanFactory beanFactory) {
        return externalMigrationGroup -> beanFactory.getBean(ExternalMigrationPerformingTask.class, externalMigrationGroup);
    }

    @Bean
    @Autowired
    public UnitUnpackTaskFactory unitUnpackTaskFactory(BeanFactory beanFactory) {
        return packedUnit -> beanFactory.getBean(UnitUnpackTask.class, packedUnit);
    }

    @Bean
    public ExecutionPhaseFactory stepPhaseFactory() {
        return new OrderedPhasesFactory(
            asList(
                PRE__UNITS_UNPACKING,
                PRE__UNIT_PROPERTIES_UPDATE,
                PRE__INTERNAL_MIGRATION_PROCESSING,
                PRE__SHARE_UPDATED_UNIT_PROPERTIES,
                PRE__STRUCTURE_DISTRIBUTION,
                SYNC,
                MAIN__AGENTS_STEP,
                MAIN___CONTROL_AGENT_STEP,
                MAIN__CONDITION_CHECK,
                POST__FINALIZATION,
                POST__EXTERNAL_MIGRATION_PROCESSING
            )
        );
    }

}
