package org.hage.platform.config;

import lombok.Data;
import lombok.ToString;
import org.hage.platform.communication.address.node.NodeAddress;

@Data
@ToString
public class ConfigurationAllocation {
    private final ComputationConfiguration configuration;
    private final NodeAddress destinationNode;
}
