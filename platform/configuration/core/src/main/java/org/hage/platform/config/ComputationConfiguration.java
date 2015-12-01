package org.hage.platform.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.HabitatGeography;

import java.io.Serializable;
import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.empty;

@Builder
@EqualsAndHashCode
public final class ComputationConfiguration implements Serializable {

    private final Collection<IComponentDefinition> globalComponents;
    private final Collection<IComponentDefinition> localComponents;
    @Getter
    private final HabitatGeography habitatGeography;

    public Collection<IComponentDefinition> getComponentsDefinitions() {
        return concat(
                ofNullable(globalComponents).map(Collection::stream).orElse(empty()),
                ofNullable(localComponents).map(Collection::stream).orElse(empty())
        ).collect(toList());
    }

    public Collection<IComponentDefinition> getGlobalComponents() {
        return globalComponents;
    }

    public Collection<IComponentDefinition> getLocalComponents() {
        return localComponents;
    }
}
