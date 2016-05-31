package org.hage.platform.component.loadbalance.partition;

import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@ToString
public class Partition {
    private List<KnapsackAllocation> knapsacks;

    Partition(Collection<KnapsackAllocation> unbalancedKnapsacks) {
        this.knapsacks = new ArrayList<>(nullSafe(unbalancedKnapsacks));
    }

}
