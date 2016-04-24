package org.hage.platform.component.execution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.execution.CancelableTaskScheduler.TaskCancelHandle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
public class NodeExecutionCore implements ExecutionCore {

    private CancelableTaskScheduler scheduler = new CancelableTaskScheduler();
    private CoreState currentState = CoreState.STOPPED;

    private Optional<TaskCancelHandle> stepTaskCancelHandle = empty();

    @Autowired
    private ExecutionStepRunnable stepRunnable;


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
        stepTaskCancelHandle = of(scheduler.scheduleContinuously(stepRunnable));
        currentState = CoreState.RUNNING;

        log.info("Core started");
    }

    private void doPause() {
        stepTaskCancelHandle.ifPresent(TaskCancelHandle::cancelAndWaitToComplete);
        stepTaskCancelHandle = empty();
        currentState = CoreState.PAUSED;

        log.info("Core paused");
    }

    private void doStop() {
        stepTaskCancelHandle.ifPresent(TaskCancelHandle::cancelAndWaitToComplete);
        stepTaskCancelHandle = empty();
        stepRunnable.reset();
        currentState = CoreState.STOPPED;

        log.info("Core stopped");
    }

    @Override
    public ExecutionInfo getInfo() {
        return new ExecutionInfo(stepRunnable.getPerformedStepsCount());
    }

}
