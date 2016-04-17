package org.hage.platform.component.simulationconfig;

import lombok.Data;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.runtime.init.ContainerConfiguration;
import org.hage.platform.component.structure.StructureDefinition;

import java.io.Serializable;

@Data
public class Common implements Serializable {
    private final ContainerConfiguration containerConfiguration;
    private final ComputationRatingConfig ratingConfig;
    private final StructureDefinition structureDefinition;
}
