package org.hage.platform.config.provider;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.HabitatOrganizationDefinition;
import org.hage.platform.rate.model.ComputationRatingConfig;

import java.util.Collection;

@Builder//TODO
public final class Configuration {
    @Getter
    private final ComputationRatingConfig computationRatingConfig;
    @Getter
    private final Collection<IComponentDefinition> globalComponents;
    @Getter
    private final Collection<IComponentDefinition> localComponents;
    @Getter
    private final HabitatOrganizationDefinition habitatConfiguration;
}
