package org.hage.performance.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hage.address.node.NodeAddress;
import org.hage.performance.node.measure.PerformanceRate;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NodeCombinedPerformance implements Serializable {
    private final NodeAddress address;
    private final PerformanceRate performanceRate;
}
