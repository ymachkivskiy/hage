package org.hage.platform.config;

import java.util.Optional;

public interface PlatformConfigurationValueProvider {
    <T> Optional<T> getValueOf(ConfigurationItem item, Class<T> valueClass);
}
