package org.hage.platform.node.runtime.container;

import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.util.Optional;

public interface StopConditionProvider {
    Optional<StopCondition> getStopCondition();
}
