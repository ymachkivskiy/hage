package org.hage.platform.component.execution.core;

import org.hage.platform.component.execution.core.executor.CoreBatchExecutor;
import org.hage.platform.component.execution.core.executor.ExecutionPhasesProvider;
import org.hage.platform.component.execution.core.executor.ParallelCoreBatchExecutor;
import org.hage.platform.component.execution.core.supplier.FixedExecutionPhasesProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.hage.platform.component.simulation.structure.CellExecutionPhase.AGENTS_STEP;

@Configuration
@ComponentScan(basePackageClasses = PlatformExecutionCoreModuleConfiguration.class)
class PlatformExecutionCoreModuleConfiguration {

    @Bean
    public CoreBatchExecutor coreBatchExecutor() {
        return new ParallelCoreBatchExecutor();
    }

    @Bean
    public ExecutionPhasesProvider coreExecutorPhasesProvider() {
        return new FixedExecutionPhasesProvider(
            AGENTS_STEP
        );
    }

    @Bean
    public ExecutionCore executionCore() {
        return new BaseNodeExecutionCore();
    }

}
