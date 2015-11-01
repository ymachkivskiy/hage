package org.hage.performance.cluster;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@ToString
public class AggregatedPerformanceMeasurements {
    private List<NodeCombinedPerformance> nodesRateCombinedPerformances;

    public AggregatedPerformanceMeasurements(List<NodeCombinedPerformance> nodesRateCombinedPerformances) {
        this.nodesRateCombinedPerformances = new ArrayList<>(nodesRateCombinedPerformances);
    }

    public List<NodeCombinedPerformance> getNodesRateCombinedPerformances() {
        return unmodifiableList(nodesRateCombinedPerformances);
    }
}
