package org.hage.platform.config.distribution.endpoint;

import lombok.Data;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.config.ComputationConfiguration;

@Data
public class AllocationPart {
    private final ComputationConfiguration config;
    private final NodeAddress address;
}
