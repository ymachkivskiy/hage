package org.hage.platform.rate.local.normalize.scaled;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.rate.local.normalize.GlobalRateSettings;
import org.hage.platform.rate.local.normalize.PerformanceRate;
import org.hage.platform.rate.local.normalize.RateNormalizer;
import org.hage.platform.rate.model.RateCalculationSettings;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_CEILING;
import static java.math.BigDecimal.valueOf;

@Slf4j
@AllArgsConstructor
class ScaledRateNormalizer implements RateNormalizer {
    private final GlobalRateSettings globalRateConfiguration;
    private final RateCalculationSettings rateConfig;

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
