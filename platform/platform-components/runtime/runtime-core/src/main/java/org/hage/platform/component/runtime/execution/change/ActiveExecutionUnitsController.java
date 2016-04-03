package org.hage.platform.component.runtime.execution.change;

import org.hage.platform.component.runtime.execution.ExecutionUnit;

import java.util.List;

public interface ActiveExecutionUnitsController {
    void activate(List<? extends ExecutionUnit> activatedUnits);

    void activate(ExecutionUnit activatedUnit);

    void deactivate(List<? extends ExecutionUnit> deactivatedUnits);
}
