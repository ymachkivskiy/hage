package org.hage.platform.simconf;

public interface ConfigurationStorage {
    boolean hasConfiguration();

    void updateConfiguration(Configuration configuration);
}
