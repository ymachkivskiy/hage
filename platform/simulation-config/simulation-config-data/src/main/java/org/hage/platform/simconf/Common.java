package org.hage.platform.simconf;

import lombok.Data;
import org.hage.platform.cluster.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.node.runtime.init.ContainerConfiguration;
import org.hage.platform.node.structure.StructureDefinition;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.io.Serializable;

@Data
public class Common implements Serializable {
    private final LoadBalanceConfig loadBalanceConfig;
    private final ContainerConfiguration containerConfiguration;
    private final StructureDefinition structureDefinition;
    private final Class<? extends StopCondition> stopCondition;
    private final Class<? extends UnitPropertiesStateComponent> unitPropertiesConfiguratorClazz;
}
