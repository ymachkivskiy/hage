package org.hage.platform.cluster.connection.config;

import lombok.Data;

@Data
public class ConnectionConfiguration {
    private boolean useSpecificInterface;
    private String networkInterfaceAddress;
}
