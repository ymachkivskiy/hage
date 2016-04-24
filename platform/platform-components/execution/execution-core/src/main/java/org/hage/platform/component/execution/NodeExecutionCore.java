package org.hage.platform.component.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

@Slf4j
public class NodeExecutionCore implements ExecutionCore {

    private static final String NAME_PREFIX = "exec-core-t-";

    private final ScheduledExecutorService executorService = newSingleThreadScheduledExecutor(new CustomizableThreadFactory(NAME_PREFIX));
    private Optional<TaskCancelationHolder> stepFutureHandle = empty();


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
        stepFutureHandle = of(schedule());
        currentState = CoreState.RUNNING;

        log.info("Core started");
    }

    private void doPause() {
        stepFutureHandle.ifPresent(TaskCancelationHolder::cancelAndWaitToComplete);
        stepFutureHandle = empty();
        currentState = CoreState.PAUSED;

        log.info("Core paused");
    }

    private void doStop() {
        stepFutureHandle.ifPresent(TaskCancelationHolder::cancelAndWaitToComplete);
        stepFutureHandle = empty();
        stepRunnable.reset();
        currentState = CoreState.STOPPED;

        log.info("Core stopped");
    }

    @Override
    public ExecutionInfo getInfo() {
        return new ExecutionInfo(stepRunnable.getPerformedStepsCount());
    }


    private TaskCancelationHolder schedule() {
        Semaphore semaphore = new Semaphore(1);

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new BlockingRunnableWrapper(stepRunnable, semaphore), 0, 1, MICROSECONDS);
        return new TaskCancelationHolder(semaphore, scheduledFuture);
    }

    @RequiredArgsConstructor
    private static class BlockingRunnableWrapper implements Runnable {

        private final Runnable runnable;
        private final Semaphore semaphore;


        @Override
        public void run() {
            try {
                semaphore.acquire();
                runnable.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }

    @RequiredArgsConstructor
    private static class TaskCancelationHolder {
        private final Semaphore semaphore;
        private final ScheduledFuture<?> future;

        public void cancelAndWaitToComplete() {
            future.cancel(false);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

    }

}
