package org.hage.platform.simconf;

import org.hage.platform.node.rate.model.ComputationRatingConfig;

public interface ConfigurationDistributor {
    void distributeUsingRatingConfiguration(Configuration configuration, ComputationRatingConfig ratingConfig);
}
