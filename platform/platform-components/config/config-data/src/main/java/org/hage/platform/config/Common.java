package org.hage.platform.config;

import lombok.Data;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.structure.StructureDefinition;

import java.io.Serializable;
import java.util.Collection;

@Data
public class Common implements Serializable {
    private final ComputationRatingConfig ratingConfig;
    private final Collection<IComponentDefinition> globalComponents;
    private final StructureDefinition structureDefinition;
}
