package org.hage.platform.config;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.definition.IComponentDefinition;

import java.io.Serializable;
import java.util.Collection;

@Builder
@EqualsAndHashCode
@ToString
public class ComputationConfiguration implements Serializable {

    private final Collection<IComponentDefinition> globalComponents;
    @Getter
    private final HabitatGeography habitatGeography;

    public Collection<IComponentDefinition> getComponentsDefinitions() {
        return globalComponents;
    }

    public Collection<IComponentDefinition> getGlobalComponents() {
        return globalComponents;
    }
}
