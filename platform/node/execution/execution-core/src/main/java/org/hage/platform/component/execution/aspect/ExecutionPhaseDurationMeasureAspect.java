package org.hage.platform.component.execution.aspect;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionDurationObserver;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import static java.time.Duration.ofNanos;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Slf4j
@SingletonComponent
@Aspect
@Order(1)
class ExecutionPhaseDurationMeasureAspect {

    private final Stopwatch stopwatch = Stopwatch.createUnstarted();

    @Autowired
    private ExecutionDurationObserver phaseDurationObserver;

    @Before("org.hage.platform.component.execution.aspect.Pointcuts.stepPhaseExecution()")
    private void startStopWatch() {
        stopwatch.reset();
        stopwatch.start();
    }

    @AfterThrowing(value = "org.hage.platform.component.execution.aspect.Pointcuts.stepPhaseExecution() && args(phase,..)", throwing = "exception")
    private void logExceptionDuringPhaseExecution(ExecutionPhase phase, Throwable exception) {
        log.error("Error during phase " + phase.getType() + " execution", exception);
    }

    @After("org.hage.platform.component.execution.aspect.Pointcuts.stepPhaseExecution() && args(phase,..)")
    private void measurePhaseExecutionDuration(ExecutionPhase phase) {
        stopwatch.stop();

        log.debug("Phase \"{}\" execution duration [{}]", phase.getType(), stopwatch);

        phaseDurationObserver.refreshPhaseDuration(phase.getType(), ofNanos(stopwatch.elapsed(NANOSECONDS)));
    }


}
