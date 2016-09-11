package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;

public interface ConfigurationSupplier {

    String configurationName();

    InputConfiguration getConfiguration();
}
