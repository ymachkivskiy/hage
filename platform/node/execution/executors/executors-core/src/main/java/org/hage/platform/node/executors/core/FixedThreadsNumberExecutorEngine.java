package org.hage.platform.node.executors.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.hage.platform.node.executors.ExecutorUtils;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

// TODO: 09.03.16 move all executors to one place executors module
class FixedThreadsNumberExecutorEngine implements ExecutionEngine {
    private static final String THREADS_PREFIX = "exec-core-t-%d";

    private final ExecutorService executor;

    public FixedThreadsNumberExecutorEngine(int threadsNumber) {

        ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat(THREADS_PREFIX)
            .setUncaughtExceptionHandler((t, e) -> {
                System.err.print("Exception thrown by " + t);
                e.printStackTrace();
            }).build();

        this.executor = new ThreadPoolExecutor(
            threadsNumber,
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
