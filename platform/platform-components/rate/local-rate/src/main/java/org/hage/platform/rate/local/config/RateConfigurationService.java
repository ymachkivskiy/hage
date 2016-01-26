package org.hage.platform.rate.local.config;

import com.google.common.eventbus.Subscribe;
import org.hage.platform.config.event.ConfigurationUpdatedEvent;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RateConfigurationService implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Autowired
    private RatingSettingsProvider ratingSettingsProvider;

    private AtomicReference<RatingSettings> finalSettings = new AtomicReference<>();

    public RatingSettings getSettings() {
        return finalSettings.get();
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    @PostConstruct
    private void initBaseSettings() {
        finalSettings.set(ratingSettingsProvider.getBaseConfig());
    }

    private class PrivateEventListener implements EventListener {

        @SuppressWarnings("unused")
        @Subscribe
        public void onComputationConfigurationUpdated(ConfigurationUpdatedEvent event) {
            finalSettings.set(ratingSettingsProvider.getUpdatedConfig(event.getComputationConfiguration().getRatingConfig()));
        }
    }

}
