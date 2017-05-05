package org.hage.platform.simconf.division;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.simconf.Specific;
import org.hage.util.proportion.Countable;
import org.hage.util.proportion.Division;
import org.hage.util.proportion.Proportions;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.annotation.Autowired;

@SingletonComponent
public class SpecificDivisor implements ProportionsDivisor<Specific, Countable> {

    @Autowired
    private ProportionsDivisor<Population, Countable> populationDivisor;

    @Override
    public Division<Specific> divideUsingProportions(Specific source, Proportions<Countable> proportions) {

        Division<Population> organizationDivision = populationDivisor.divideUsingProportions(source.getPopulation(), proportions);

        return organizationDivision.translateTo(
            Specific::new
        );

    }
}
