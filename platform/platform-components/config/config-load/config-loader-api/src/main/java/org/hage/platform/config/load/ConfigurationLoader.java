package org.hage.platform.config.load;


import org.hage.platform.config.load.definition.Configuration;

public interface ConfigurationLoader {
    Configuration load() throws ConfigurationNotFoundException;
}
