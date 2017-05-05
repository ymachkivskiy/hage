package org.hage.platform.simconf.load.xml;

import java.util.Optional;

public interface ConfigurationFilePathProvider {
    Optional<String> getConfigurationFilePath();
}
