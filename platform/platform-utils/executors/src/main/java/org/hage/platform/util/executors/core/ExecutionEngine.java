package org.hage.platform.util.executors.core;

import java.util.Collection;

interface ExecutionEngine {
    void executeAll(Collection<? extends Runnable> tasks);
}
