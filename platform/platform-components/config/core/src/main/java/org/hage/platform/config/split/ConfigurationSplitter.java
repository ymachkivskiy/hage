package org.hage.platform.config.split;

import org.hage.platform.config.def.ComputationConfiguration;
import org.hage.util.proportion.Proportions;

public interface ConfigurationSplitter {
    ConfigurationDivision split(ComputationConfiguration configuration, Proportions proportions);
}
