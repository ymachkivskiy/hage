package org.hage.platform.cluster.loadbalance.check;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.function.Supplier;

import static java.time.Duration.ZERO;

@Slf4j
@RequiredArgsConstructor
class ExecutionDurationCheckStrategy implements BalanceCheckStrategy {

    private final int secondsToWait;
    private final Supplier<Duration> executionDurationSupplier;

    private Duration lastPositiveDuration = ZERO;

    @Override
    public boolean shouldCheckBalance() {
        Duration duration = executionDurationSupplier.get();

        boolean shouldPerformBalanceCheck = duration.minus(lastPositiveDuration).getSeconds() >= secondsToWait;

        log.info("Check if need balance for current execution time {} (balancing every {} seconds): {}", duration, secondsToWait, shouldPerformBalanceCheck);

        if (shouldPerformBalanceCheck) {
            lastPositiveDuration = duration;
        }

        return shouldPerformBalanceCheck;
    }

}
