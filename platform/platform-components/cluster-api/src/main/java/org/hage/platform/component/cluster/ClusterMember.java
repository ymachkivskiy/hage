package org.hage.platform.component.cluster;

import lombok.Data;

@Data
public class ClusterMember {
    private final boolean isLocal;
    private final NodeAddress nodeAddress;
    private final int memberIndex;
}
