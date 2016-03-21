package org.hage.platform.config.distribution.endpoint;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.config.Configuration;

@Data
public class AllocationPart {
    private final Configuration config;
    private final NodeAddress address;
}
