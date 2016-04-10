package org.hage.platform.component.simulationconfig.division;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.simulationconfig.ConfigurationDivisor;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.Specific;
import org.hage.util.proportion.Division;
import org.hage.util.proportion.Proportions;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@HageComponent
@Slf4j
public class BaseConfigurationDivisor implements ConfigurationDivisor {

    @Autowired
    private ProportionsDivisor<Specific> specificDivisor;

    @Override
    public Division<Configuration> divideUsingProportions(Configuration source, Proportions proportions) {
        log.debug("Divide configuration '{}' using proportions '{}'", source, proportions);

        Division<Specific> specificDivision = specificDivisor.divideUsingProportions(source.getSpecific(), proportions);

        return specificDivision.translateTo(
            specific -> new Configuration(source.getCommon(), specific)
        );
    }

}
