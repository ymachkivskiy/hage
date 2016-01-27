package org.hage.platform.config.distributed;

import lombok.Data;
import lombok.ToString;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.util.communication.address.node.NodeAddress;

@Data
@ToString
public class ConfigurationAllocation {
    private final ComputationConfiguration configuration;
    private final NodeAddress destinationNode;
}
