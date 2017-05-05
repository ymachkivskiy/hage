package org.hage.platform.cluster.connection.remote.config;

import org.hage.platform.cluster.connection.config.ConnectionConfiguration;
import org.hage.platform.cluster.connection.config.ConnectionConfigurationProvider;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.PlatformConfigurationValueProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class RemoteConnectionConfigurationProvider implements ConnectionConfigurationProvider, ConfigurationCategorySupplier {

    private static final NetworkInterfaceConfigurationItem networkInterfaceAddrConfItem = new NetworkInterfaceConfigurationItem();

    @Autowired
    private PlatformConfigurationValueProvider configurationProvider;

    @Override
    public ConnectionConfiguration getConfiguration() {
        ConnectionConfiguration resultConfig = new ConnectionConfiguration();

        Optional<String> networkInterfaceAddr = configurationProvider.getValueOf(networkInterfaceAddrConfItem, String.class);

        networkInterfaceAddr.ifPresent(addr -> {
            resultConfig.setUseSpecificInterface(true);
            resultConfig.setNetworkInterfaceAddress(addr);
        });

        return resultConfig;
    }

    @Override
    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory(
            "Connection configuration",
            "Configuration for Hage cluster network connection.",
            singletonList(networkInterfaceAddrConfItem)
        );
    }

}
