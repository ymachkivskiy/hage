package org.hage.platform.config.local.division;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.util.proportion.Proportions;

public interface ConfigurationSplitter {
    ConfigurationDivision split(ComputationConfiguration configuration, Proportions proportions);
}
