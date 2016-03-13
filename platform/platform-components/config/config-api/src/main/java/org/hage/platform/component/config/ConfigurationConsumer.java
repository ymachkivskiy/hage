package org.hage.platform.component.config;

import org.hage.platform.config.Configuration;

public interface ConfigurationConsumer {

    void acceptConfiguration(Configuration configuration);

}
