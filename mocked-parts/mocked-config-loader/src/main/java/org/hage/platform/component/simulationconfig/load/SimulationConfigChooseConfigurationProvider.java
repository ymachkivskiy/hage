package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.PlatformConfigurationValueProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class SimulationConfigChooseConfigurationProvider implements ConfigurationCategorySupplier, TestConfigurationSupplier {

    @Autowired
    private List<ConfigurationSupplier> supportedConfigsSuppliers;
    @Autowired
    private SimulationChooseConfigItem simulationChooseConfigItem;
    @Autowired
    private PlatformConfigurationValueProvider configurationValueProvider;

    @Override
    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory(
            "Simulation configuration",
            "Choose simulation configuration params",
            singletonList(simulationChooseConfigItem)
        );
    }

    @Override
    public InputConfiguration getInputConfiguration() {
        return configurationValueProvider.getValueOf(simulationChooseConfigItem, String.class)
            .map(this::getConfiguration)
            .orElseThrow(() -> new HageRuntimeException("not supported simulation config"));
    }

    private InputConfiguration getConfiguration(String configName) {

        for (ConfigurationSupplier supplier : supportedConfigsSuppliers) {
            if (supplier.configurationName().equals(configName)) {
                return supplier.getConfiguration();
            }
        }

        return null;
    }

}
