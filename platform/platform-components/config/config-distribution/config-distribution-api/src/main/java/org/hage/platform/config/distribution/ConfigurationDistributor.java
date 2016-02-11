package org.hage.platform.config.distribution;

import org.hage.platform.config.ComputationConfiguration;

public interface ConfigurationDistributor {
    void distribute(ComputationConfiguration configuration);
}
