package org.jage.performance.rate;

import lombok.Getter;
import org.jage.address.node.NodeAddress;

import java.io.Serializable;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public class ClusterNode implements Serializable {
    @Getter
    private final NodeAddress address;
    @Getter
    private Optional<CombinedPerformanceRate> performanceRateInfo = empty();

    public ClusterNode(NodeAddress nodeAddress) {
        this.address = nodeAddress;
    }

    public void setPerformanceRateInfo(CombinedPerformanceRate rate) {
        this.performanceRateInfo = ofNullable(rate);
    }
}
