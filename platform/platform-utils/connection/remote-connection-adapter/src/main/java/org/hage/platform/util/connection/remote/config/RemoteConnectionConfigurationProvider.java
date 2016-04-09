package org.hage.platform.util.connection.remote.config;

import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.ConfigurationProvider;
import org.hage.platform.util.connection.config.ConnectionConfiguration;
import org.hage.platform.util.connection.config.ConnectionConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class RemoteConnectionConfigurationProvider implements ConnectionConfigurationProvider, ConfigurationCategorySupplier {

    private static final NetworkInterfaceConfigurationItem networkInterfaceAddrConfItem = new NetworkInterfaceConfigurationItem();

    @Autowired
    private ConfigurationProvider configurationProvider;

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
