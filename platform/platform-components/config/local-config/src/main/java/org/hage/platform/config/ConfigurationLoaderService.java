package org.hage.platform.config;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.config.event.ConfigurationLoadRequestEvent;
import org.hage.platform.config.event.ConfigurationLoadedEvent;
import org.hage.platform.config.provider.ComputationConfigurationProvider;
import org.hage.platform.util.bus.EventBus;
import org.hage.platform.util.bus.EventListener;
import org.hage.platform.util.bus.EventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ConfigurationLoaderService implements EventSubscriber {

    private final EventListener eventListener = new PrivateEventListener();

    @Autowired
    private ComputationConfigurationProvider configurationLoader;

    @Autowired
    private EventBus eventBus;


    private void notifyConfigurationLoaded(ComputationConfiguration configuration) {
        log.info("Notify configuration has been loaded {}", configuration);

        eventBus.post(new ConfigurationLoadedEvent(configuration));
    }

    @Override
    public EventListener getEventListener() {
        return eventListener;
    }

    private class PrivateEventListener implements EventListener {

        @Subscribe
        @SuppressWarnings("unused")
        public void onConfigurationLoadRequest(ConfigurationLoadRequestEvent event) {
            configurationLoader.tryGetConfiguration().ifPresent(ConfigurationLoaderService.this::notifyConfigurationLoaded);
        }

    }
}