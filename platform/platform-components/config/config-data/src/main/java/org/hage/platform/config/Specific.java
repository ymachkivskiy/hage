package org.hage.platform.config;

import lombok.Data;
import org.hage.platform.component.simulation.structure.SimulationOrganization;

import java.io.Serializable;

@Data
public class Specific implements Serializable {
    private final SimulationOrganization simulationOrganization;
}
