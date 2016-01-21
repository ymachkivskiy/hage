package org.hage.platform.config.local.provider;


import org.hage.platform.config.ComputationConfiguration;

import java.util.Optional;

public interface ComputationConfigurationProvider {
    Optional<ComputationConfiguration> tryGetConfiguration();
}
