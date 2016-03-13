package org.hage.platform.config;

import lombok.Data;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.rate.model.ComputationRatingConfig;

import java.io.Serializable;
import java.util.Collection;

@Data
public class Common implements Serializable {
    private final ComputationRatingConfig ratingConfig;
    private final Collection<IComponentDefinition> globalComponents;
}
