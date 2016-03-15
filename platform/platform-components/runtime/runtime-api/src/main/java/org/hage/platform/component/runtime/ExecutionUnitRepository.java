package org.hage.platform.component.runtime;

import org.hage.platform.component.runtime.unit.ClusterUnitAddress;
import org.hage.platform.component.runtime.unit.ExecutionUnit;

public interface ExecutionUnitRepository {

    boolean isValid(ClusterUnitAddress address);

    boolean isLocalUnit(ClusterUnitAddress address);

    ExecutionUnit getLocalExecutionUnit(ClusterUnitAddress address);

}
