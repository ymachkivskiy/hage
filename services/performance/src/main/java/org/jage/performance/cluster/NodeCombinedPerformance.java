package org.jage.performance.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jage.address.node.NodeAddress;
import org.jage.performance.node.measure.PerformanceRate;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NodeCombinedPerformance implements Serializable {
    private final NodeAddress address;
    private final PerformanceRate performanceRate;
}
