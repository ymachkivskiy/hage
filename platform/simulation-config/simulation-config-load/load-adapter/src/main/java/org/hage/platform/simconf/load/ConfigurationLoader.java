package org.hage.platform.simconf.load;


import org.hage.platform.simconf.load.definition.InputConfiguration;

public interface ConfigurationLoader {
    InputConfiguration load() throws ConfigurationNotFoundException;
}
