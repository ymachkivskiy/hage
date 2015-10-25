package org.jage.performance.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jage.address.node.NodeAddress;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ClusterNode implements Serializable {
    private final NodeAddress address;
    private final CombinedPerformanceRate performanceRateInfo;
}
