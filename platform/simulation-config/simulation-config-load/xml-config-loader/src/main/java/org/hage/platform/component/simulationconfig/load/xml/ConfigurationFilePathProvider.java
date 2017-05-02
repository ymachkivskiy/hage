package org.hage.platform.component.simulationconfig.load.xml;

import java.util.Optional;

public interface ConfigurationFilePathProvider {
    Optional<String> getConfigurationFilePath();
}
