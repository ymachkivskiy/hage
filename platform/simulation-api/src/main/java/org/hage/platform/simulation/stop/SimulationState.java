package org.hage.platform.simulation.stop;

import lombok.Data;

import java.time.Duration;

@Data
public class SimulationState {
    private final long simulationStepsPerformed;
    private final Duration simulationDuration;
}
