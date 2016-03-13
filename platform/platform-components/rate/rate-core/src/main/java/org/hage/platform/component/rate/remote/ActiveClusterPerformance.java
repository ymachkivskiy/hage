package org.hage.platform.component.rate.remote;

import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.rate.measure.PerformanceRate;
import org.hage.platform.util.connection.NodeAddress;

import java.util.*;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;
import static org.hage.platform.component.rate.util.PerformanceRateArithmetic.sum;

@ToString
public class ActiveClusterPerformance {

    private final Map<NodeAddress, PerformanceRate> nodeRates;
    @Getter
    private final PerformanceRate overallPerformance;
    @Getter
    private final Collection<NodeAbsolutePerformance> nodeAbsolutePerformances;

    public ActiveClusterPerformance(Map<NodeAddress, PerformanceRate> nodeRates) {
        this.nodeRates = new HashMap<>(nodeRates);
        this.overallPerformance = sum(this.nodeRates.values());
        this.nodeAbsolutePerformances = calculateAbsolutePerformances(this.nodeRates);
    }

    public Set<NodeAddress> measuredNodes() {
        return unmodifiableSet(nodeRates.keySet());
    }

    public PerformanceRate getRateFor(NodeAddress address) {
        return nodeRates.get(address);
    }

    private List<NodeAbsolutePerformance> calculateAbsolutePerformances(Map<NodeAddress, PerformanceRate> nodeRates) {
        return nodeRates.entrySet().stream()
            .map(e -> new NodeAbsolutePerformance(e.getKey(), e.getValue()))
            .collect(toList());
    }

}
