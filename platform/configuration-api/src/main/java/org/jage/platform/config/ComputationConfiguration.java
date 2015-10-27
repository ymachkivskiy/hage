package org.jage.platform.config;

import lombok.Builder;
import org.jage.platform.component.definition.IComponentDefinition;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import static java.util.Collections.unmodifiableCollection;
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

    public Collection<IComponentDefinition> getGlobalComponents() {
        return unmodifiableCollection(globalComponents);
    }

    public Collection<IComponentDefinition> getLocalComponents() {
        return unmodifiableCollection(localComponents);
    }

    @Override
    public String toString() {
        //TODO implement computation configuration description
        return super.toString();
    }
}
