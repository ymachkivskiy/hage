package org.hage.platform.util.executors;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newCachedThreadPool;

class Worker implements WorkerExecutor {

    private final ExecutorService executorService;

    public Worker() {
        this.executorService = newCachedThreadPool(new CustomizableThreadFactory("worker-thread-"));
    }

    @Override
    public void submit(Runnable command) {
        executorService.execute(command);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

}
