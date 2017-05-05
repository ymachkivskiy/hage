package org.hage.platform.simconf.endpoint;

import lombok.Data;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.simconf.Configuration;

@Data
public class AllocationPart {
    private final Configuration config;
    private final NodeAddress address;
}
