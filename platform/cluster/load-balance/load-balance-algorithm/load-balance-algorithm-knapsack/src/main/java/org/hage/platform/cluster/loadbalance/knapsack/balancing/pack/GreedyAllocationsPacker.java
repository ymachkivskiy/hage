package org.hage.platform.cluster.loadbalance.knapsack.balancing.pack;

import org.hage.platform.cluster.loadbalance.knapsack.balancing.KnapsackAllocation;
import org.hage.platform.cluster.loadbalance.knapsack.model.Item;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

public class GreedyAllocationsPacker implements AllocationsPacker {

    @Override
    public Collection<KnapsackAllocation> repack(Collection<KnapsackAllocation> knapsackAllocations) {

        List<KnapsackAllocation> workingCopyOfAllocations = copyAllocations(knapsackAllocations);

        reBalance(getUnbalanced(workingCopyOfAllocations));

        return workingCopyOfAllocations;
    }

    private void reBalance(Collection<KnapsackAllocation> unbalancedAllocations) {

        for (KnapsackAllocation currAlloc : unbalancedAllocations) {
            if (currAlloc.isUnderCapacity()) {
                continue;
            }

            Iterator<Item> orderedItemsIterator = getOrderedAllocationItemsIterator(currAlloc);

            while (currAlloc.isOverCapacity() && orderedItemsIterator.hasNext()) {

                Item itemToMoveCandidate = orderedItemsIterator.next();
                KnapsackAllocation bestTargetAlloc = currAlloc;

                long currAllocSpreadAfterItemMoved = originAllocationSpreadAfterMove(currAlloc, itemToMoveCandidate);
                long bestAllocSpread = Long.MAX_VALUE;

                for (KnapsackAllocation targetAllocCandidate : unbalancedAllocations) {

                    if (targetAllocCandidate == currAlloc || targetAllocCandidate.isOverCapacity()) {
                        continue;
                    }

                    long summarySpreadAfterItemMoved = targetAllocationSpreadAfterMove(targetAllocCandidate, itemToMoveCandidate) + currAllocSpreadAfterItemMoved;

                    if (summarySpreadAfterItemMoved < min(currAlloc.getBalanceDiff() + targetAllocCandidate.getBalanceDiff(), bestAllocSpread)) {
                        bestAllocSpread = summarySpreadAfterItemMoved;
                        bestTargetAlloc = targetAllocCandidate;
                    }

                    if (summarySpreadAfterItemMoved == 0) {
                        break;
                    }

                }

                moveItemIfAllocationsDifferent(currAlloc, itemToMoveCandidate, bestTargetAlloc);
            }
        }

    }

    private void moveItemIfAllocationsDifferent(KnapsackAllocation currAlloc, Item itemToMoveCandidate, KnapsackAllocation bestTarget) {

        if (bestTarget != currAlloc) {
            currAlloc.getKnapsack().removeItem(itemToMoveCandidate);
            bestTarget.getKnapsack().addItem(itemToMoveCandidate);
        }

    }

    private long originAllocationSpreadAfterMove(KnapsackAllocation allocation, Item item) {
        return Math.abs(allocation.getCapacity() - (allocation.getKnapsack().getSize() - item.getSize()));
    }

    private long targetAllocationSpreadAfterMove(KnapsackAllocation allocation, Item item) {
        return Math.abs(allocation.getCapacity() - (allocation.getKnapsack().getSize() + item.getSize()));
    }

    private List<KnapsackAllocation> copyAllocations(Collection<KnapsackAllocation> knapsackAllocations) {
        return knapsackAllocations.stream()
            .map(KnapsackAllocation::copy)
            .collect(toList());
    }

    private Iterator<Item> getOrderedAllocationItemsIterator(KnapsackAllocation currAlloc) {
        return currAlloc.getKnapsack().getItems()
            .stream()
            .sorted((i1, i2) -> i2.getSize() - i1.getSize())
            .collect(toList())
            .iterator();
    }

    private Collection<KnapsackAllocation> getUnbalanced(Collection<KnapsackAllocation> knapsackAllocations) {
        return knapsackAllocations.stream()
            .filter(allocation -> !allocation.isBalanced())
            .collect(toList());
    }

}
