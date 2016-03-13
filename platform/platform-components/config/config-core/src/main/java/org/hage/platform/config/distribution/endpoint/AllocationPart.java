package org.hage.platform.config.distribution.endpoint;

import lombok.Data;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.config.Configuration;

@Data
public class AllocationPart {
    private final Configuration config;
    private final NodeAddress address;
}
