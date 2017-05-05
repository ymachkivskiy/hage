package org.hage.platform.component.simulationconfig;

import org.hage.platform.node.rate.model.ComputationRatingConfig;

public interface ConfigurationDistributor {
    void distributeUsingRatingConfiguration(Configuration configuration, ComputationRatingConfig ratingConfig);
}
