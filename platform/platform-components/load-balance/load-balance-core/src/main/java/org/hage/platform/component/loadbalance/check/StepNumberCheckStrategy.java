package org.hage.platform.component.loadbalance.check;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

import static java.lang.Math.floorMod;

@Slf4j
@RequiredArgsConstructor
class StepNumberCheckStrategy implements BalanceCheckStrategy {

    private final int stepsGap;
    private final Supplier<Long> performedStepsNumberSupplier;

    @Override
    public boolean shouldCheckBalance() {
        Long stepsPerformed = performedStepsNumberSupplier.get();
        boolean shouldCheckBalance = floorMod(stepsPerformed, stepsGap) == 0;

        log.info("Check if need balance in current step {} (balancing every {}'th step): {}", stepsPerformed, stepsGap, shouldCheckBalance);

        return shouldCheckBalance;
    }

}
