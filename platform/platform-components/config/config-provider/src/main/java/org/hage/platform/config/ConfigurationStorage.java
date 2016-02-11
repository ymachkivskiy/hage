package org.hage.platform.config;

public interface ConfigurationStorage {
    boolean hasConfiguration();

    void updateConfiguration(ComputationConfiguration configuration);
}
