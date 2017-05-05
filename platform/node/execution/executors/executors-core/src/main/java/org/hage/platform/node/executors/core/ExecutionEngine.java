package org.hage.platform.node.executors.core;

import java.util.Collection;

interface ExecutionEngine {
    void executeAll(Collection<? extends Runnable> tasks);
}
