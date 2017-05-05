package org.hage.platform.cluster.api;

import lombok.Data;

@Data
public class ClusterMember {
    private final boolean isLocal;
    private final NodeAddress nodeAddress;
    private final int memberIndex;
}
