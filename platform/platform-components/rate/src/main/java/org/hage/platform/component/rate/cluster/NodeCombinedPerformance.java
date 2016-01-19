package org.hage.platform.component.rate.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hage.address.node.NodeAddress;
import org.hage.platform.component.rate.node.measure.PerformanceRate;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class NodeCombinedPerformance implements Serializable {
    private final NodeAddress address;
    private final PerformanceRate performanceRate;
}
