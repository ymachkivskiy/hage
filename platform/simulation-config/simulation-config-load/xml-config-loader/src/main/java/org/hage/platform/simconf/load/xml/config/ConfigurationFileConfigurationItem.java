package org.hage.platform.simconf.load.xml.config;

import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;
import org.hage.platform.config.ConfigurationValueCheckException;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;

class ConfigurationFileConfigurationItem extends ConfigurationItem {

    protected ConfigurationFileConfigurationItem() {
        super(new ConfigurationItemProperties(
                false,
                "c",
                "sim-config",
                "Path to simulation configuration file",
                true,
                String.class,
                "path",
                null
        ));
    }

    @Override
    public void checkValue(Object value) throws ConfigurationValueCheckException {

        if (value instanceof String) {

            String path = (String) value;

            if (!Files.exists(Paths.get(path))) {
                throw new ConfigurationValueCheckException(format("Given configuration file '%s' does not exist", path));
            }
        }

    }

}
