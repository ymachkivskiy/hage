package org.hage.platform.component.runtime.unit;

public interface ExecutionUnit {
    ClusterUnitAddress getAddress();

    void performAgentsStep();

    void performControlAgentStep();
}
