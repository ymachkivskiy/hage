package org.hage.platform.component.runtime.execution;

public interface ExecutionUnit {
    String getUniqueIdentifier();

    void performControlAgentStep();

    void performAgentsStep();

    void afterStepPerformed();
}
