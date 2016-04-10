package org.hage.platform.component.rate;

import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.rate.config.RatingSettingProvider;
import org.hage.platform.component.rate.config.RatingSettingsResolver;
import org.hage.platform.component.rate.config.data.RatingSettings;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@HageComponent
class RateSettingsStorageService implements RatingSettingProvider, RateConfigurationConsumer {

    @Autowired
    private RatingSettingsResolver ratingSettingsResolver;

    private AtomicReference<ComputationRatingConfig> computationRateSettingsHolder = new AtomicReference<>();

    @Override
    public RatingSettings getSettings() {
        return ofNullable(computationRateSettingsHolder.get())
            .map(ratingSettingsResolver::getSettingsUsing)
            .orElse(ratingSettingsResolver.getSettings());
    }

    @Override
    public void acceptRateConfiguration(ComputationRatingConfig ratingConfig) {
        computationRateSettingsHolder.set(ratingConfig);
    }

}
