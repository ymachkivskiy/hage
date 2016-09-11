package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;
import org.hage.platform.config.ConfigurationValueCheckException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SimulationChooseConfigItem extends ConfigurationItem {

    public static final String NON_CONFIG = "none";
    @Autowired
    private List<ConfigurationSupplier> supportedConfigsSuppliers;

    public SimulationChooseConfigItem() {
        super(new ConfigurationItemProperties(
            true,
            "sc",
            "simulation-conf",
            "Provide simulation configuration for node",
            true,
            String.class,
            "CONFIGURATION",
            NON_CONFIG
        ));
    }

    @Override
    public void checkValue(Object defaultValue) throws ConfigurationValueCheckException {
        if (NON_CONFIG.equals(defaultValue))
            return;

        Set<String> supportedConfigs = supportedConfigsSuppliers.stream().map(ConfigurationSupplier::configurationName).collect(toSet());

        if (!(defaultValue instanceof String) || !supportedConfigs.contains(defaultValue)) {
            throw new ConfigurationValueCheckException("Not supported simulation configuration name '" + Objects.toString(defaultValue) + "'. Supprted are only " + supportedConfigs);
        }
    }
}
