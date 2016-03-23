package org.hage.platform.component.rate;

import org.hage.platform.component.rate.model.ComputationRatingConfig;

public interface RateConfigurationConsumer {
    void acceptRateConfiguration(ComputationRatingConfig ratingConfig);
}
