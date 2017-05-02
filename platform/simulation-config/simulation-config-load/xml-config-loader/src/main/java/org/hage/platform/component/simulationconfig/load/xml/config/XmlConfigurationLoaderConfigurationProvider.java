package org.hage.platform.component.simulationconfig.load.xml.config;

import org.hage.platform.component.simulationconfig.load.xml.ConfigurationFilePathProvider;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.PlatformConfigurationValueProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class XmlConfigurationLoaderConfigurationProvider  implements ConfigurationCategorySupplier, ConfigurationFilePathProvider{

    private static final ConfigurationFileConfigurationItem fileConfigurationItem = new ConfigurationFileConfigurationItem();

    @Autowired
    private PlatformConfigurationValueProvider configurationProvider;


    @Override
    public Optional<String> getConfigurationFilePath() {
        return configurationProvider.getValueOf(fileConfigurationItem, String.class);
    }

    @Override
    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory(
                "Simulation",
                "Configuration for Hage simulation",
                singletonList(fileConfigurationItem)
        );
    }
}
