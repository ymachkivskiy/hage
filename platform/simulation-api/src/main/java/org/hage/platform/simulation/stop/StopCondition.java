package org.hage.platform.simulation.stop;

import java.io.Serializable;

public interface StopCondition extends Serializable {
    boolean satisfied(SimulationState state);
}
