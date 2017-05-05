package org.hage.platform.node.execution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.node.execution.step.StepTask;
import org.hage.platform.node.executors.schedule.ContinuousSerialScheduler;
import org.hage.platform.node.executors.schedule.ScheduledTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
public class NodeExecutionCore implements ExecutionCore {

    private CoreState currentState = CoreState.STOPPED;

    private ScheduledTaskHandle stepTaskHandle;

    @Autowired
    private StepTask stepTask;
    @Autowired
    private ContinuousSerialScheduler continuousSerialScheduler;

    @PostConstruct
    private void initScheduledTask() {
        stepTaskHandle = continuousSerialScheduler.registerTask(stepTask);
    }

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
        stepTaskHandle.resumeIfNotRunning();
        currentState = CoreState.RUNNING;

        log.info("Core started");
    }

    private void doPause() {
        stepTaskHandle.cancelAndWaitToComplete();
        currentState = CoreState.PAUSED;

        log.info("Core paused");
    }

    private void doStop() {
        stepTaskHandle.cancelAndWaitToComplete();
        stepTask.reset();
        currentState = CoreState.STOPPED;

        log.info("Core stopped");
    }

}
