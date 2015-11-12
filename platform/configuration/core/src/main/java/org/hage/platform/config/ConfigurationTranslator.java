package org.hage.platform.config;

import org.hage.platform.config.loader.Configuration;

public interface ConfigurationTranslator {
    ComputationConfiguration translate(Configuration configuration);
}
