package org.hage.platform.config.distribution.division;

import lombok.Data;
import lombok.ToString;
import org.hage.platform.communication.address.node.NodeAddress;
import org.hage.platform.config.ComputationConfiguration;

@Data
@ToString
public class ConfigurationAllocation {
    private final ComputationConfiguration configuration;
    private final NodeAddress destinationNode;
}
