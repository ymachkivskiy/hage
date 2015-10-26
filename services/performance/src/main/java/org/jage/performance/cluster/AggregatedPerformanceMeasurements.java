package org.jage.performance.cluster;

import lombok.ToString;
import org.jage.performance.rate.ClusterNode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@ToString
public class AggregatedPerformanceMeasurements {
    private List<ClusterNode> clusterNodesRates;

    public AggregatedPerformanceMeasurements(List<ClusterNode> clusterNodesRates) {
        this.clusterNodesRates = new ArrayList<>(clusterNodesRates);
    }

    public List<ClusterNode> getClusterNodesRates() {
        return unmodifiableList(clusterNodesRates);
    }
}
