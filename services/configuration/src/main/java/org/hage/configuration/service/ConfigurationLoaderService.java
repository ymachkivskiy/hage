package org.hage.configuration.service;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.hage.configuration.event.ConfigurationLoadRequestEvent;
import org.hage.configuration.event.ConfigurationLoadedEvent;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.provider.ComputationConfigurationProvider;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;


@ThreadSafe
@Slf4j
public class ConfigurationLoaderService implements IStatefulComponent {

    @Autowired
    private ComputationConfigurationProvider configurationLoader;

    @Autowired
    private EventBus eventBus;

    @Override
    public void init() {
    }

    @Override
    public boolean finish() {
        return true;
    }

    @Subscribe
    public void onConfigurationLoadRequest(@Nonnull final ConfigurationLoadRequestEvent event) {
        configurationLoader.tryGetConfiguration().ifPresent(this::notifyConfigurationLoaded);
    }

    private void notifyConfigurationLoaded(ComputationConfiguration configuration) {
        log.info("Notify configuration has been loaded {}", configuration);

        eventBus.post(new ConfigurationLoadedEvent(configuration));
    }
}
