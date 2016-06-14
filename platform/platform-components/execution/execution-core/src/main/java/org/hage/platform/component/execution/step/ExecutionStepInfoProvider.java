package org.hage.platform.component.execution.step;

public interface ExecutionStepInfoProvider {
    long getPerformedStepsCount();

    long getCurrentStepNumber();
}
