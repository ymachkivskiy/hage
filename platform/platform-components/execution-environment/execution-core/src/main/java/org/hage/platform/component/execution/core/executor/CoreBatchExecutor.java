package org.hage.platform.component.execution.core.executor;

import java.util.Collection;

public interface CoreBatchExecutor {

    /**
     * Blocks util all tasks are finished
     *
     * @param tasks tasks to perform
     */
    void executeAll(Collection<? extends Runnable> tasks);
}
