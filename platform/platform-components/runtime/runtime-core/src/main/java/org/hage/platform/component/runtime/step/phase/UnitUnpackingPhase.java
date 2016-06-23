package org.hage.platform.component.runtime.step.phase;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.execution.phase.ExecutionPhase;
import org.hage.platform.component.execution.phase.ExecutionPhaseType;
import org.hage.platform.component.runtime.unitmove.PackedUnitsProvider;
import org.hage.platform.component.runtime.unitmove.UnitUnpackTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.execution.phase.ExecutionPhaseType.PRE__UNITS_UNPACKING;

@SingletonComponent
public class UnitUnpackingPhase implements ExecutionPhase {

    @Autowired
    private PackedUnitsProvider packedUnitsProvider;
    @Autowired
    private UnitUnpackTaskFactory unitUnpackTaskFactory;

    @Override
    public ExecutionPhaseType getType() {
        return PRE__UNITS_UNPACKING;
    }

    @Override
    public Collection<? extends Runnable> getTasks(long currentStep) {
        return packedUnitsProvider.takePackedUnits()
            .stream()
            .map(unitUnpackTaskFactory::createTask)
            .collect(toList());
    }

}
