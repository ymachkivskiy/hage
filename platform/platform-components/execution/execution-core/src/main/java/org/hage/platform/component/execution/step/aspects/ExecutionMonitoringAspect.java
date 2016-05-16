package org.hage.platform.component.execution.step.aspects;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionMonitor;
import org.hage.platform.component.execution.step.ExecutionStepRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Slf4j
@SingletonComponent
@Aspect
@Order(0)
public class ExecutionMonitoringAspect implements ExecutionMonitor {

    private final AtomicReference<Duration> accumulatedDuration = new AtomicReference<>(Duration.ZERO);
    private final Stopwatch stopwatch = Stopwatch.createUnstarted();

    @Autowired
    private ExecutionStepRunnable stepRunnable;

    @Before("Pointcuts.stepPerforming()")
    private void startTimerBeforeStepStart() {
        log.debug("Start timer before start of step execution");
        stopwatch.reset();
        stopwatch.start();
    }

    @After("Pointcuts.stepPerforming()")
    private void stopTimerAfterStepFinish() {
        stopwatch.stop();

        accumulatedDuration.updateAndGet(currentDuration -> currentDuration.plusNanos(stopwatch.elapsed(NANOSECONDS)));

        log.debug("Stop timer after finish of step execution in: {}", stopwatch);
    }


    @Override
    public long getPerformedStepsCount() {
        return stepRunnable.getPerformedStepsCount();
    }

    @Override
    public long getCurrentStepNumber() {
        return stepRunnable.getCurrentStepNumber();
    }

    @Override
    public Duration getExecutionDuration() {
        return accumulatedDuration.get().plusNanos(stopwatch.elapsed(NANOSECONDS));
    }

}
