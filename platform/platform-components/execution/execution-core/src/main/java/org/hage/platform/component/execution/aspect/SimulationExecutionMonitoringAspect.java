package org.hage.platform.component.execution.aspect;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.monitor.ExecutionDurationObserver;
import org.hage.platform.component.execution.monitor.SimulationExecutionMonitor;
import org.hage.platform.component.execution.step.ExecutionStepInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Stopwatch.createUnstarted;
import static java.time.Duration.ZERO;
import static java.time.Duration.ofNanos;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@Slf4j
@SingletonComponent
@Aspect
@Order(0)
class SimulationExecutionMonitoringAspect implements SimulationExecutionMonitor {

    private final AtomicReference<Duration> accumulatedDuration = new AtomicReference<>(ZERO);
    private final Stopwatch stopwatch = createUnstarted();

    @Autowired
    private ExecutionStepInfoProvider stepRunnable;
    @Autowired
    private ExecutionDurationObserver executionDurationObserver;

    @Before("Pointcuts.stepPerforming()")
    private void startTimerBeforeStepStart() {
        log.debug("Start timer before start of step execution");
        stopwatch.reset();
        stopwatch.start();
    }

    @After("Pointcuts.stepPerforming()")
    private void stopTimerAfterStepFinish() {
        stopwatch.stop();

        Duration stepDuration = ofNanos(stopwatch.elapsed(NANOSECONDS));

        accumulatedDuration.updateAndGet(currentDuration -> currentDuration.plus(stepDuration));

        log.debug("Stop timer after finish of step execution in: {}", stopwatch);

        executionDurationObserver.refreshStepDuration(stepDuration);
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
    public Duration getSimulationExecutionDuration() {
        return accumulatedDuration.get().plusNanos(stopwatch.elapsed(NANOSECONDS));
    }

}
