package org.hage.platform.cluster.loadbalance.check;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.hage.util.CollectionUtils.nullSafeCopy;

@Slf4j
class ConjunctionCheckStrategyComposite implements BalanceCheckStrategy {
    private final List<BalanceCheckStrategy> strategies;

    public ConjunctionCheckStrategyComposite(List<BalanceCheckStrategy> strategies) {
        this.strategies = nullSafeCopy(strategies);
    }

    @Override
    public boolean shouldCheckBalance() {
        log.debug("Checking if all inner strategies say that check should be performed");
        return strategies.stream().allMatch(BalanceCheckStrategy::shouldCheckBalance);
    }

}
