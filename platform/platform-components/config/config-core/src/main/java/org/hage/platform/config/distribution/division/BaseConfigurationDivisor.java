package org.hage.platform.config.distribution.division;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.config.ConfigurationDivisor;
import org.hage.platform.config.Configuration;
import org.hage.platform.config.Specific;
import org.hage.util.proportion.ProportionDivision;
import org.hage.util.proportion.Proportions;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseConfigurationDivisor implements ConfigurationDivisor {

    @Autowired
    private ProportionsDivisor<Specific> specificDivisor;

    @Override
    public ProportionDivision<Configuration> divideUsingProportions(Configuration source, Proportions proportions) {
        log.debug("Divide configuration '{}' using proportions '{}'", source, proportions);

        ProportionDivision<Specific> specificDivision = specificDivisor.divideUsingProportions(source.getSpecific(), proportions);

        return specificDivision.translateTo(
            specific -> new Configuration(source.getCommon(), specific)
        );
    }

}
