package org.hage.platform.component.simulationconfig.division;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.simulationconfig.Specific;
import org.hage.util.proportion.Division;
import org.hage.util.proportion.Proportions;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class SpecificDivisor implements ProportionsDivisor<Specific> {

    @Autowired
    private ProportionsDivisor<Population> organizationDivisor;

    @Override
    public Division<Specific> divideUsingProportions(Specific source, Proportions proportions) {

        Division<Population> organizationDivision = organizationDivisor.divideUsingProportions(source.getPopulation(), proportions);

        return organizationDivision.translateTo(
            Specific::new
        );

    }
}
