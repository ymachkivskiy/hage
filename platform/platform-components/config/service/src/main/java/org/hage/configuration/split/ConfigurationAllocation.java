package org.hage.configuration.split;

import lombok.Data;
import lombok.ToString;
import org.hage.address.node.NodeAddress;
import org.hage.platform.config.ComputationConfiguration;

@Data
@ToString
public class ConfigurationAllocation {
    private final ComputationConfiguration configuration;
    private final NodeAddress destinationNode;
}
