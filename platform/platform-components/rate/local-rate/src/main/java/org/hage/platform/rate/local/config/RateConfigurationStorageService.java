package org.hage.platform.rate.local.config;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.rate.model.ComputationRatingConfig;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

@Component
public class RateConfigurationStorageService implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Autowired
    private RatingSettingsProvider ratingSettingsProvider;

    private AtomicReference<ComputationRatingConfig> computationRateSettingsHolder = new AtomicReference<>();

    public RatingSettings getSettings() {
        return ofNullable(computationRateSettingsHolder.get())
                .map(ratingSettingsProvider::getSettingsUsing)
                .orElse(ratingSettingsProvider.getSettings());
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @SuppressWarnings("unused")
        @Subscribe
        public void onComputationConfigurationUpdated(ConfigurationUpdatedEvent event) {
            computationRateSettingsHolder.set(event.getComputationConfiguration().getRatingConfig());
        }
    }

}
