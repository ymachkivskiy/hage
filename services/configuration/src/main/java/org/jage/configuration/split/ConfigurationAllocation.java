package org.jage.configuration.split;

import lombok.Data;
import lombok.ToString;
import org.jage.address.node.NodeAddress;
import org.jage.platform.config.ComputationConfiguration;

@Data
@ToString
public class ConfigurationAllocation {
    private final ComputationConfiguration configuration;
    private final NodeAddress destinationNode;
}
