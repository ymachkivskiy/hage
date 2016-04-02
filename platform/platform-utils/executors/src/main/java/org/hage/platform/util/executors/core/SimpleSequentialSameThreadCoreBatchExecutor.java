package org.hage.platform.util.executors.core;

import org.hage.platform.util.executors.core.CoreBatchExecutor;

import java.util.Collection;

public class SimpleSequentialSameThreadCoreBatchExecutor implements CoreBatchExecutor {

    @Override
    public void executeAll(Collection<? extends Runnable> tasks) {
        tasks.forEach(Runnable::run);
    }

}
