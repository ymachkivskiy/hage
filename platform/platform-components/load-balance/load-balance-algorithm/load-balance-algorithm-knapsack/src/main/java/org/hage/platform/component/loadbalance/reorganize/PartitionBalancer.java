package org.hage.platform.component.loadbalance.reorganize;

import org.hage.platform.component.loadbalance.partition.Partition;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PartitionBalancer {

    public List<KnapsackTransfer> balancePartition(Partition partition) {
        //todo : NOT IMPLEMENTED
        return Collections.emptyList();
    }

}
