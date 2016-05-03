package org.hage.platform.util.executors.simple;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class Worker implements WorkerExecutor {

    private final ExecutorService executorService;

    public Worker() {

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("worker-t").setUncaughtExceptionHandler((t, e) -> {
            System.err.print("Exception thrown by " + t);
            e.printStackTrace();
        }).build();

        this.executorService = newCachedThreadPool(factory);
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
