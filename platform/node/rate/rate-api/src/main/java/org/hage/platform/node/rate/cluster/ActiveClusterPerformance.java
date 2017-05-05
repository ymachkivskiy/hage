package org.hage.platform.node.rate.cluster;

import lombok.Getter;
import lombok.ToString;
import org.hage.platform.cluster.api.NodeAddress;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

@ToString
public class ActiveClusterPerformance {

    @Getter
    private final Collection<NodeAbsolutePerformance> nodeAbsolutePerformances;

    public ActiveClusterPerformance(Map<NodeAddress, PerformanceRate> nodeRates) {
        this.nodeAbsolutePerformances = unmodifiableList(calculateAbsolutePerformances(nodeRates));
    }

    private List<NodeAbsolutePerformance> calculateAbsolutePerformances(Map<NodeAddress, PerformanceRate> nodeRates) {
        return nodeRates.entrySet().stream()
            .map(e -> new NodeAbsolutePerformance(e.getKey(), e.getValue()))
            .collect(toList());
    }

}
