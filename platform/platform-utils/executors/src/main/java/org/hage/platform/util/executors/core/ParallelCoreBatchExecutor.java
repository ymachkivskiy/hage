package org.hage.platform.util.executors.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.hage.platform.util.executors.ExecutorUtils;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO: 09.03.16 move all executors to one place executors module
public class ParallelCoreBatchExecutor implements CoreBatchExecutor {
    private static final String THREADS_PREFIX = "exec-core-t-";

    private final ExecutorService executor;

    public ParallelCoreBatchExecutor() {
        ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat(THREADS_PREFIX)
            .setUncaughtExceptionHandler((t, e) -> {
                System.err.print("Exception thrown by " + t);
                e.printStackTrace();
            }).build();

        this.executor = new ThreadPoolExecutor(
            getRuntime().availableProcessors(),
            Integer.MAX_VALUE, //unused when queue for tasks is unbounded
            10L, MILLISECONDS,
            new LinkedBlockingDeque<>(),
            factory
        );
    }

    @Override
    public void executeAll(Collection<? extends Runnable> tasks) {
        try {
            executor.invokeAll(ExecutorUtils.toCallable(tasks));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
