package org.hage.platform.node.execution.step;

public interface ExecutionStepInfoProvider {
    long getPerformedStepsCount();

    long getCurrentStepNumber();
}
