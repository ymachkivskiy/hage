package org.hage.platform.config.distribution.division;

import org.hage.platform.config.ComputationConfiguration;
import org.hage.util.proportion.Countable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

// TODO: 10.03.16 change
public final class ConfigurationDivision {
    private Map<Countable, ComputationConfiguration> splitMap;

    ConfigurationDivision(Map<Countable, ComputationConfiguration> splitMap) {
        this.splitMap = new HashMap<>(splitMap);
    }

    public Optional<ComputationConfiguration> getFor(Countable countable) {
        return ofNullable(splitMap.get(countable));
    }
}
