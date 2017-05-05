package org.hage.platform.component.simulationconfig.load.definition;

import lombok.Builder;
import lombok.Getter;
import org.hage.platform.node.container.definition.IComponentDefinition;
import org.hage.platform.cluster.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.node.rate.model.ComputationRatingConfig;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.util.Collection;

@Builder//TODO change
public final class InputConfiguration {
    @Getter
    private final LoadBalanceConfig loadBalanceConfig;
    @Getter
    private final ComputationRatingConfig computationRatingConfig;
    @Getter
    private final Collection<IComponentDefinition> globalComponents;
    @Getter
    private final SimulationOrganizationDefinition simulationDefinition;
    @Getter
    private final Class<? extends StopCondition> stopConditionClazz;
    @Getter
    private final Class<? extends UnitPropertiesStateComponent> propertiesConfiguratorClazz;
}
