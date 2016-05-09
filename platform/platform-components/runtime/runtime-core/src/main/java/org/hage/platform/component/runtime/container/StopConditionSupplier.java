package org.hage.platform.component.runtime.container;

import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.util.Optional;

public interface StopConditionSupplier {
    Optional<StopCondition> getStopCondition();
}
