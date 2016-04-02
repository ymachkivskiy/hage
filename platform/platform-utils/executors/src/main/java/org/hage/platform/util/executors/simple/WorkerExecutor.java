package org.hage.platform.util.executors.simple;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface WorkerExecutor {
    void submit(Runnable command);

    <T> Future<T> submit(Callable<T> task);
}
