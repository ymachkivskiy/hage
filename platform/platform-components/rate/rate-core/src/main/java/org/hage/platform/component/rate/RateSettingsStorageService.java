package org.hage.platform.component.rate;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.component.rate.config.RatingSettingsResolver;
import org.hage.platform.component.rate.config.data.RatingSettings;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@Component
public class RateSettingsStorageService implements EventSubscriber {

    @Autowired
    private RatingSettingsResolver ratingSettingsResolver;

    private AtomicReference<ComputationRatingConfig> computationRateSettingsHolder = new AtomicReference<>();

    public RatingSettings getSettings() {
        return ofNullable(computationRateSettingsHolder.get())
                .map(ratingSettingsResolver::getSettingsUsing)
                .orElse(ratingSettingsResolver.getSettings());
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onComputationConfigurationUpdated(ConfigurationUpdatedEvent event) {
        computationRateSettingsHolder.set(event.getConfiguration().getCommon().getRatingConfig());
    }

}
