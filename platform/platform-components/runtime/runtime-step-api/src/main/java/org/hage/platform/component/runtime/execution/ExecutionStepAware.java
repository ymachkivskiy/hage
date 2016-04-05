package org.hage.platform.component.runtime.execution;

public interface ExecutionStepAware {
    void onStepPerformed(long stepNumber);

    PostStepPhase getPhase();
}
