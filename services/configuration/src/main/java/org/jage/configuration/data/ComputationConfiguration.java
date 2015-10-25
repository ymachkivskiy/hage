package org.jage.configuration.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jage.platform.component.definition.IComponentDefinition;

import java.io.Serializable;
import java.util.Collection;

@AllArgsConstructor
public class ComputationConfiguration implements Serializable {
    @Getter
    private final Collection<IComponentDefinition> components;
}
