package org.hage.platform.component.config;

import org.hage.platform.config.Configuration;

public interface ConfigurationStorage {
    boolean hasConfiguration();

    void updateConfiguration(Configuration configuration);
}
