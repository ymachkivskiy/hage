package org.hage.platform.component.runtime.unit;

import java.io.Serializable;

public interface ExecutionUnit extends Serializable {
    ExecutionUnitAddress getAddress();

    void performAgentsStep();

    void performControlAgentStep();
}
