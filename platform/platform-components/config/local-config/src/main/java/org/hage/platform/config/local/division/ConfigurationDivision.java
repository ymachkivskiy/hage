package org.hage.platform.config.local.division;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.util.proportion.Countable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public final class ConfigurationDivision {
    private Map<Countable, ComputationConfiguration> splitMap;

    ConfigurationDivision(Map<Countable, ComputationConfiguration> splitMap) {
        this.splitMap = new HashMap<>(splitMap);
    }

    public Optional<ComputationConfiguration> get(Countable countable) {
        return ofNullable(splitMap.get(countable));
    }
}
