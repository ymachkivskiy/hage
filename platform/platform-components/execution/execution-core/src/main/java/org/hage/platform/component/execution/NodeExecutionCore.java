package org.hage.platform.component.execution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

@Slf4j
public class NodeExecutionCore implements ExecutionCore {

    private static final String NAME_PREFIX = "exec-core-t-";

    private final ScheduledExecutorService executorService = newSingleThreadScheduledExecutor(new CustomizableThreadFactory(NAME_PREFIX));
    private Optional<ScheduledFuture<?>> stepFutureHandle = empty();


    @Autowired
    private ExecutionStepRunnable stepRunnable;

    private CoreState currentState = CoreState.STOPPED;


    @Override
    public synchronized void start() {
        log.info("Request for start execution node while in state: {}", currentState);

        if (currentState == CoreState.PAUSED || currentState == CoreState.STOPPED) {
            doStart();
        } else {
            log.warn("Starting execution core is not permitted while in state: {}", currentState);
        }
    }

    @Override
    public synchronized void pause() {
        log.info("Request for pause execution core while in state: {}", currentState);

        if (currentState == CoreState.RUNNING) {
            doPause();
        } else {
            log.warn("Pausing execution core is not permitted while in state: {}", currentState);
        }
    }

    @Override
    public synchronized void stop() {
        log.info("Request for stop execution core while in state: {}", currentState);

        if (currentState == CoreState.RUNNING || currentState == CoreState.PAUSED) {
            doStop();
        } else {
            log.warn("Stopping execution core is not permitted while in state: {}", currentState);
        }
    }

    private void doStart() {
        stepFutureHandle = of(executorService.scheduleAtFixedRate(stepRunnable, 0, 1, MICROSECONDS));
        currentState = CoreState.RUNNING;

        log.info("Core started");
    }

    private void doPause() {
        stepFutureHandle.ifPresent(f -> f.cancel(false));
        stepFutureHandle = empty();
        currentState = CoreState.PAUSED;

        log.info("Core paused");
    }

    private void doStop() {
        stepFutureHandle.ifPresent(f -> f.cancel(false));
        stepFutureHandle = empty();
        stepRunnable.reset();
        currentState = CoreState.STOPPED;

        log.info("Core stopped");
    }


    @Override
    public ExecutionInfo getInfo() {
        return new ExecutionInfo(stepRunnable.getPerformedStepsCount());
    }

}
