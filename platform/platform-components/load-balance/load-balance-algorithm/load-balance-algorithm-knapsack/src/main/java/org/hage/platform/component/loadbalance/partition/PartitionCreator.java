package org.hage.platform.component.loadbalance.partition;

import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartitionCreator {

    public Partition createPartition(List<Knapsack> knapsacks) {
        //todo : NOT IMPLEMENTED
        return new Partition();
    }


}
