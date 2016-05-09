package org.hage.platform.component.runtime.init;

import lombok.Data;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.io.Serializable;
import java.util.Collection;

import static org.hage.util.CollectionUtils.nullSafeCopy;

@Data
public class ContainerConfiguration implements Serializable {
    private final Collection<IComponentDefinition> globalComponents;
    private final ControlAgentDefinition controlAgentDefinition;
    private final Collection<AgentDefinition> agentDefinitions;
    private final Class<? extends StopCondition> stopCondition;

    public ContainerConfiguration(Collection<AgentDefinition> agentDefinitions, ControlAgentDefinition controlAgentDefinition, Collection<IComponentDefinition> globalComponents, Class<? extends StopCondition> stopCondition) {
        this.globalComponents = nullSafeCopy(globalComponents);
        this.agentDefinitions = nullSafeCopy(agentDefinitions);
        this.controlAgentDefinition = controlAgentDefinition;
        this.stopCondition = stopCondition;
    }

}
