package org.hage.platform.simconf.load.config;

import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.PlatformConfigurationValueProvider;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class LoadConfigurationProvider implements LoadingConfiguration, ConfigurationCategorySupplier {

    private static final NodeRoleConfigurationItem nodeRoleConfigItem = new NodeRoleConfigurationItem();

    @Autowired
    private PlatformConfigurationValueProvider configurationValueProvider;

    @Override
    public boolean shouldLoadSimulationConfiguration() {
        return configurationValueProvider.getValueOf(nodeRoleConfigItem, String.class)
            .map(nodeRoleConfigItem::isMasterRole)
            .orElse(false);
    }

    @Override
    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory(
            "Node",
            "Node configuration options",
            singletonList(nodeRoleConfigItem)
        );
    }

}
