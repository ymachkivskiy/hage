package org.hage.platform.config;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.HabitatInternalConfiguration;

import java.io.Serializable;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.empty;

@Builder
public final class ComputationConfiguration implements Serializable {

    private final Collection<IComponentDefinition> globalComponents;
    private final Collection<IComponentDefinition> localComponents;
    @Getter
    private final HabitatInternalConfiguration habitatConfiguration;

    public Collection<IComponentDefinition> getComponentsDefinitions() {
        return concat(
                ofNullable(globalComponents).map(Collection::stream).orElse(empty()),
                ofNullable(localComponents).map(Collection::stream).orElse(empty())
        ).collect(toList());
    }

    public Collection<IComponentDefinition> getGlobalComponents() {
        return unmodifiableCollection(globalComponents);
    }

    public Collection<IComponentDefinition> getLocalComponents() {
        return unmodifiableCollection(localComponents);
    }
}
