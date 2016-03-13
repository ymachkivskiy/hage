package org.hage.platform.config.distribution.division;

import org.hage.platform.component.simulation.structure.SimulationOrganization;
import org.hage.platform.config.Specific;
import org.hage.util.proportion.ProportionDivision;
import org.hage.util.proportion.Proportions;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecificDivisor implements ProportionsDivisor<Specific> {

    @Autowired
    private ProportionsDivisor<SimulationOrganization> organizationDivisor;

    @Override
    public ProportionDivision<Specific> divideUsingProportions(Specific source, Proportions proportions) {

        ProportionDivision<SimulationOrganization> organizationDivision = organizationDivisor.divideUsingProportions(source.getSimulationOrganization(), proportions);

        return organizationDivision.translateTo(
            Specific::new
        );

    }
}
