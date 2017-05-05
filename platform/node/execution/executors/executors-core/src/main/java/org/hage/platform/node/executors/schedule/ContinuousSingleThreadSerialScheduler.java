package org.hage.platform.node.executors.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.hage.util.concurrency.Locks.withLock;
import static org.hage.util.concurrency.Locks.withLockAndRuntimeExceptions;

@Slf4j
@SingletonComponent
public class ContinuousSingleThreadSerialScheduler implements ContinuousSerialScheduler {

    private final Lock handlesLock = new ReentrantLock();
    private final LinkedList<Handle> handles = new LinkedList<>();

    private final Lock notificationLock = new ReentrantLock();
    private final Condition notificationCondition = notificationLock.newCondition();
    private final AtomicBoolean shouldShutDown = new AtomicBoolean(false);
    private final SchedulerThread schedulerThread;

    public ContinuousSingleThreadSerialScheduler() {
        schedulerThread = new SchedulerThread();
        schedulerThread.start();
    }

    @Override
    public ScheduledTaskHandle registerTask(ScheduleTask task) {
        return withLockAndRuntimeExceptions(handlesLock, () -> {
            Handle h = new Handle(task);
            handles.add(h);
            return h;
        });
    }


    @Override
    public void shutdown() {

        shouldShutDown.set(true);

        withLock(notificationLock, notificationCondition::signal);

        try {
            schedulerThread.join();
        } catch (InterruptedException e) {
            log.error("Error while stopping scheduler thread", e);
        }
    }

    @RequiredArgsConstructor
    private class Handle implements ScheduledTaskHandle {

        private Semaphore completionSemaphore = new Semaphore(1);
        private final ScheduleTask task;
        private final AtomicBoolean enabled = new AtomicBoolean(false);

        @Override
        public void cancelAndWaitToComplete() {
            try {
                completionSemaphore.acquire();
                enabled.set(false);
                completionSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void resumeIfNotRunning() {
            enabled.set(true);
            withLock(notificationLock, notificationCondition::signal);
        }

        public boolean performScheduledTask() {
            try {
                completionSemaphore.acquire();

                if (!enabled.get()) {
                    return false;
                }

                return task.perform();

            } catch (InterruptedException e) {
                log.error("Error during acquiring task completion semaphore", e);
                return false;
            } finally {
                completionSemaphore.release();
            }
        }

    }

    private class SchedulerThread extends Thread {


        public SchedulerThread() {
            super("core-continuous-scheduler");
        }

        @Override
        public void run() {

            while (!shouldShutDown.get()) {

                if (!tryPerformAtLeasOneTaskFromAllCurrentTasks()) {

                    withLock(notificationLock, () -> {
                            try {
                                notificationCondition.await();
                            } catch (InterruptedException e) {
                                log.error("Error during sleep");
                            }
                        }
                    );

                }

            }

        }

        private boolean tryPerformAtLeasOneTaskFromAllCurrentTasks() {

            List<Handle> currentCycleHandlers = withLockAndRuntimeExceptions(handlesLock, () -> new ArrayList<>(handles));

            boolean executedAtLeasOnce = false;

            try {

                for (Handle handle : currentCycleHandlers) {
                    executedAtLeasOnce = handle.performScheduledTask() || executedAtLeasOnce;
                }

            } catch (Exception e) {
                log.error("Error during scheduled tasks execution ", e);
            }

            return executedAtLeasOnce;
        }
    }


}
