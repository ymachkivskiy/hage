package org.hage.platform.component.simulation.structure;

import org.hage.platform.config.ComputationConfiguration;
import org.springframework.stereotype.Component;

@Component
public class DummyComputationStructureConfigurator implements ComputationStructureConfigurator {
    @Override
    public void configureWith(ComputationConfiguration configuration) {
        /* no-op */
    }
}
