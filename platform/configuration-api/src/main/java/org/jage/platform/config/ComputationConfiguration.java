package org.jage.platform.config;

import lombok.Builder;
import org.jage.platform.component.definition.IComponentDefinition;

import java.io.Serializable;
import java.util.Collection;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.empty;

@Builder
public class ComputationConfiguration implements Serializable {

    private final Collection<IComponentDefinition> globalComponents;
    private final Collection<IComponentDefinition> localComponents;

    public Collection<IComponentDefinition> getComponentsDefinitions() {
        return concat(
                ofNullable(globalComponents).map(Collection::stream).orElse(empty()),
                ofNullable(localComponents).map(Collection::stream).orElse(empty())
        ).collect(toList());
    }
}
