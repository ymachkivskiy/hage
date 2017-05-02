package org.hage.platform.util.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public abstract class ExecutorUtils {
    public static List<Callable<Object>> toCallable(Collection<? extends Runnable> tasks) {
        return tasks.stream()
            .map(Executors::callable)
            .collect(toList());
    }
}
