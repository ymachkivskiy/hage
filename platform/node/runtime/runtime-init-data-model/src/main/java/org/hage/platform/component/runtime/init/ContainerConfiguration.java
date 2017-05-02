package org.hage.platform.component.runtime.init;

import lombok.Data;
import org.hage.platform.component.container.definition.IComponentDefinition;

import java.io.Serializable;
import java.util.Collection;

import static org.hage.util.CollectionUtils.nullSafeCopy;

@Data
public class ContainerConfiguration implements Serializable {
    private final Collection<IComponentDefinition> globalComponents;
    private final ControlAgentDefinition controlAgentDefinition;
    private final Collection<AgentDefinition> agentDefinitions;

    public ContainerConfiguration(Collection<AgentDefinition> agentDefinitions, ControlAgentDefinition controlAgentDefinition, Collection<IComponentDefinition> globalComponents) {
        this.globalComponents = nullSafeCopy(globalComponents);
        this.agentDefinitions = nullSafeCopy(agentDefinitions);
        this.controlAgentDefinition = controlAgentDefinition;
    }

}
