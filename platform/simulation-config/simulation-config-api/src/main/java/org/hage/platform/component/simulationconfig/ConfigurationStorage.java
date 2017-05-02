package org.hage.platform.component.simulationconfig;

public interface ConfigurationStorage {
    boolean hasConfiguration();

    void updateConfiguration(Configuration configuration);
}
