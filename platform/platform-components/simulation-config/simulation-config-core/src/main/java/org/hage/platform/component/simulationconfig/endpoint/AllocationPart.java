package org.hage.platform.component.simulationconfig.endpoint;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.simulationconfig.Configuration;

@Data
public class AllocationPart {
    private final Configuration config;
    private final NodeAddress address;
}
