package org.hage.platform.simulation.runtime.stopcondition;

public interface StopCondition {
    boolean isSatisfiedBy(SimulationState state);
}
