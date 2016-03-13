package org.hage.platform.config.distribution.endpoint;

import lombok.Data;
import org.hage.platform.config.Configuration;
import org.hage.platform.util.connection.NodeAddress;

@Data
public class AllocationPart {
    private final Configuration config;
    private final NodeAddress address;
}
