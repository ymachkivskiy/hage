package org.hage.platform.util.connection.remote.config;

import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;
import org.hage.platform.config.ConfigurationValueCheckException;

class NetworkInterfaceConfigurationItem extends ConfigurationItem {

    public NetworkInterfaceConfigurationItem() {
        super(new ConfigurationItemProperties(
                false,
                "i",
                "net-if-addr",
                "Network interface address which will be used for network communication between cluster nodes.",
                true,
                String.class,
                "interface",
                null
            )
        );
    }

    @Override
    public void checkValue(Object defaultValue) throws ConfigurationValueCheckException {
        //todo : NOT IMPLEMENTED
        // TODO: throw exception for not correct value
    }
}
