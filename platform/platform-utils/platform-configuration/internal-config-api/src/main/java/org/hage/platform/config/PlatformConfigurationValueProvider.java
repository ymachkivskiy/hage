package org.hage.platform.config;

import java.util.Optional;

public interface PlatformConfigurationValueProvider {
    // TODO: change mechanism adjusting for no class passing, like it has been done in unit properties quering mechanism
    <T> Optional<T> getValueOf(ConfigurationItem item, Class<T> valueClass);
}
