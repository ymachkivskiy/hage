package org.hage.platform.rate.distributed;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hage.platform.communication.address.node.NodeAddress;
import org.hage.platform.rate.local.measure.PerformanceRate;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NodeCombinedPerformance implements Serializable {
    private final NodeAddress address;
    private final PerformanceRate performanceRate;
}
