package org.hage.platform.component.simulationconfig.load.definition;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.rate.model.ComputationRatingConfig;

import java.util.Collection;

@Builder//TODO change
public final class InputConfiguration {
    @Getter
    private final ComputationRatingConfig computationRatingConfig;
    @Getter
    private final Collection<IComponentDefinition> globalComponents;
    @Getter
    private final SimulationOrganizationDefinition simulationDefinition;
}