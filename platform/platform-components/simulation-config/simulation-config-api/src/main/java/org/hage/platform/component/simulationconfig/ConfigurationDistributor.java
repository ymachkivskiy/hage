package org.hage.platform.component.simulationconfig;

import org.hage.platform.component.rate.model.ComputationRatingConfig;

public interface ConfigurationDistributor {
    void distributeUsingRatingConfiguration(Configuration configuration, ComputationRatingConfig ratingConfig);
}
