package org.hage.platform.config.split;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.util.proportion.Proportions;

import java.util.Collections;

public class SimpleConfigurationSplitter implements ConfigurationSplitter {

    @Override
    public ConfigurationDivision split(ComputationConfiguration configuration, Proportions proportions) {
        return new ConfigurationDivision(Collections.emptyMap());
    }

}
