package org.hage.platform.config.provider;

import org.hage.platform.config.def.ComputationConfiguration;

import java.util.Optional;

public interface ComputationConfigurationProvider {
    Optional<ComputationConfiguration> tryGetConfiguration();
}
