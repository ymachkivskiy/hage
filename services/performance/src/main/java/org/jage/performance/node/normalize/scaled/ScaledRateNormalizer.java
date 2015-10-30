package org.jage.performance.node.normalize.scaled;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jage.performance.node.config.MeasurerRateConfiguration;
import org.jage.performance.node.measure.PerformanceRate;
import org.jage.performance.node.normalize.RateNormalizer;
import org.jage.performance.node.config.GlobalRateConfiguration;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.valueOf;

@Slf4j
@AllArgsConstructor
class ScaledRateNormalizer implements RateNormalizer {
    private final GlobalRateConfiguration globalRateConfiguration;
    private final MeasurerRateConfiguration rateConfig;

    @Override
    public PerformanceRate normalize(int rate) {
        log.debug("Normalizing {} rate", rate);

        BigDecimal proportion = valueOf(rate).divide(valueOf(rateConfig.getMaxRate()), 16, ROUND_CEILING);

        log.debug("Proportion is {} to max measure rate {}", proportion, rateConfig.getMaxRate());

        BigDecimal resultRate = new BigDecimal(globalRateConfiguration.getMaxGlobalRate()).multiply(proportion);

        log.debug("Result rate is {}", resultRate);

        return new PerformanceRate(resultRate.toBigInteger());
    }
}
