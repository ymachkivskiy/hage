package org.hage.platform.config.load;


import org.hage.platform.config.load.definition.InputConfiguration;

public interface ConfigurationLoader {
    InputConfiguration load() throws ConfigurationNotFoundException;
}
