package org.hage.platform.component.execution;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.MICROSECONDS;

public class CancelableTaskScheduler {

    private static final String NAME_PREFIX = "exec-scheduled-core-t-";
    private final ScheduledExecutorService executorService = newSingleThreadScheduledExecutor(new CustomizableThreadFactory(NAME_PREFIX));


    public TaskCancelHandle scheduleContinuously(Runnable task) {
        Semaphore semaphore = new Semaphore(1);

        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new TaskBlockingAdapter(task, semaphore), 0, 1, MICROSECONDS);
        return new BlockingTaskCancelHandle(semaphore, scheduledFuture);
    }


    @RequiredArgsConstructor
    private static class BlockingTaskCancelHandle implements TaskCancelHandle {

        private final Semaphore semaphore;
        private final ScheduledFuture<?> future;

        @Override
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

    @RequiredArgsConstructor
    private static class TaskBlockingAdapter implements Runnable {

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

    public interface TaskCancelHandle {
        void cancelAndWaitToComplete();
    }
}
