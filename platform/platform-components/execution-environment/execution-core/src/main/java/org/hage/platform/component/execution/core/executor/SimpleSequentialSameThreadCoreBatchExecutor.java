package org.hage.platform.component.execution.core.executor;

import java.util.Collection;

public class SimpleSequentialSameThreadCoreBatchExecutor implements CoreBatchExecutor {

    @Override
    public void executeAll(Collection<? extends Runnable> tasks) {
        tasks.forEach(Runnable::run);
    }

}
