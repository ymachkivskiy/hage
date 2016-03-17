package org.hage.platform.component.runtime;

import org.hage.platform.component.runtime.unit.ExecutionUnit;
import org.hage.platform.component.runtime.unit.ExecutionUnitAddress;

// TODO: think about interface
public interface ExecutionUnitRepository {

    boolean isValid(ExecutionUnitAddress address);

    boolean isLocalUnit(ExecutionUnitAddress address);

    ExecutionUnit getLocalExecutionUnit(ExecutionUnitAddress address);

}
