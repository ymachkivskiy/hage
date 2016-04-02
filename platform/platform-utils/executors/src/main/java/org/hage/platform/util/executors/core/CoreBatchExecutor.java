package org.hage.platform.util.executors.core;

import java.util.Collection;

public interface CoreBatchExecutor {

    /**
     * Blocks util all tasks are finished
     *
     * @param tasks tasks to perform
     */
    void executeAll(Collection<? extends Runnable> tasks);
}
