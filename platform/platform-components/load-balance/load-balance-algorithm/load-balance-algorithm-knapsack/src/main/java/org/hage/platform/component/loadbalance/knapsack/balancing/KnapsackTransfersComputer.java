package org.hage.platform.component.loadbalance.knapsack.balancing;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.loadbalance.knapsack.model.Item;
import org.hage.platform.component.loadbalance.knapsack.model.Knapsack;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class KnapsackTransfersComputer {

    public Collection<KnapsackTransfer> computeTransfers(Collection<KnapsackAllocation> originalAllocations, Collection<KnapsackAllocation> currentAllocations) {
        log.debug("Compute transfers between\noriginal : {}\ncurrent  : {}", originalAllocations, currentAllocations);

        Map<Item, Knapsack> originalItemPlacing = createItemKnapsackRelationsMap(originalAllocations);
        Map<Item, Knapsack> currentItemPlacing = createItemKnapsackRelationsMap(currentAllocations);

        checkNoItemLost(originalItemPlacing, currentItemPlacing);

        return difference(originalItemPlacing, currentItemPlacing);
    }

    private Map<Item, Knapsack> createItemKnapsackRelationsMap(Collection<KnapsackAllocation> allocations) {
        Map<Item, Knapsack> originalItemKnapsacks = new HashMap<>();

        for (KnapsackAllocation allocation : allocations) {
            Knapsack knapsack = allocation.getKnapsack();
            knapsack.getItems().forEach(i -> originalItemKnapsacks.put(i, knapsack));
        }

        return originalItemKnapsacks;
    }

    private void checkNoItemLost(Map<Item, Knapsack> originalItemPlacing, Map<Item, Knapsack> currentItemPlacing) {
        Set<Item> originalItems = originalItemPlacing.keySet();
        Set<Item> currentItems = currentItemPlacing.keySet();
        Set<Item> lostItems = Sets.difference(originalItems, currentItems);

        if (!lostItems.isEmpty()) {
            log.error("Current knapsacks allocation lost items {}", lostItems);
            throw new HageRuntimeException("Items lost during knapsack repacking");
        }

    }

    private Collection<KnapsackTransfer> difference(Map<Item, Knapsack> originalItemPlacing, Map<Item, Knapsack> currentItemPlacing) {
        return originalItemPlacing.entrySet()
            .stream()
            .map(e -> {
                Item item = e.getKey();
                Knapsack originalKnapsack = checkNotNull(e.getValue());
                Knapsack currentKnapsack = checkNotNull(currentItemPlacing.get(item));
                return new KnapsackTransfer(originalKnapsack, item, currentKnapsack);
            })
            .filter(this::isActualTransfer)
            .collect(toList());
    }

    private boolean isActualTransfer(KnapsackTransfer transfer) {
        Knapsack origin = transfer.getOrigin();
        Knapsack destination = transfer.getDestination();
        return !origin.equals(destination);
    }


}
