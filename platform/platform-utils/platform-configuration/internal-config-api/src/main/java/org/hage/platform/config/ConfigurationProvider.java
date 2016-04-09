package org.hage.platform.config;

import java.util.Optional;

public interface ConfigurationProvider {
    <T> Optional<T> getValueOf(ConfigurationItem item, Class<T> valueClass);
}
