package org.hage.platform.component.loadbalance.balancing;

import lombok.ToString;
import org.hage.platform.component.loadbalance.input.KnapsackAllocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@ToString
public class BalancingInput {
    private List<KnapsackAllocation> knapsacks;

    public BalancingInput(Collection<KnapsackAllocation> unbalancedKnapsacks) {
        this.knapsacks = new ArrayList<>(nullSafe(unbalancedKnapsacks));
    }

}
