package org.hage.platform.config.loader;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.HabitatExternalConfiguration;

import java.util.Collection;

@Builder//TODO
public final class Configuration {
    @Getter
    private final Collection<IComponentDefinition> globalComponents;
    @Getter
    private final Collection<IComponentDefinition> localComponents;
    @Getter
    private final HabitatExternalConfiguration habitatConfiguration;
}
