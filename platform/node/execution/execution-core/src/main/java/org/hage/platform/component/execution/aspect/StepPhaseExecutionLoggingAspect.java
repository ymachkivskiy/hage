package org.hage.platform.component.execution.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.StepPhaseExecutor;
import org.springframework.core.annotation.Order;

import static org.slf4j.LoggerFactory.getLogger;

@SingletonComponent
@Aspect
@Order(0)
class StepPhaseExecutionLoggingAspect {

    @Before(value = "org.hage.platform.component.execution.aspect.Pointcuts.stepPhaseExecution() && target(executor) && args(phase, step)", argNames = "executor,phase,step")
    private void logPhaseExecutionStart(StepPhaseExecutor executor, ExecutionPhase phase, long step) {

        getLogger(executor.getClass()).info("   ------ Start executing phase \"{}\" in step [{}] -------   ", phase.getType().getDescription(), step);

    }

    @AfterReturning(value = "org.hage.platform.component.execution.aspect.Pointcuts.stepPhaseExecution() && target(executor) && args(phase, step)", argNames = "executor,phase,step")
    private void logPhaseExecutionFinish(StepPhaseExecutor executor, ExecutionPhase phase, long step) {

        getLogger(executor.getClass()).info("   ------ Finish executing phase \"{}\" in step [{}] ------   ", phase.getType().getDescription(), step);

    }

}
