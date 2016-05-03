package org.hage.platform.util.executors.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;

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
            executor.invokeAll(toCallable(tasks));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Callable<Object>> toCallable(Collection<? extends Runnable> tasks) {
        return tasks.stream()
            .map(Executors::callable)
            .collect(toList());
    }


}
