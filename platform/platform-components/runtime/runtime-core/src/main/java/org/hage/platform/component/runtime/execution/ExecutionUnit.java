package org.hage.platform.component.runtime.execution;

public interface ExecutionUnit {
    String getUnitId();

    void performControlAgentStep();

    void performAgentsStep();

    void afterStepPerformed();
}
