package org.hage.platform.component.loadbalance.check;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
class ClusterSizeCheckStrategy implements BalanceCheckStrategy {

    private final Supplier<Integer> clusterSizeSupplier;

    @Override
    public boolean shouldCheckBalance() {
        boolean containsMoreThanOneNode = clusterSizeSupplier.get() > 1;

        log.info("Check if cluster contains more than one node: {}", containsMoreThanOneNode);

        return containsMoreThanOneNode;
    }

}
