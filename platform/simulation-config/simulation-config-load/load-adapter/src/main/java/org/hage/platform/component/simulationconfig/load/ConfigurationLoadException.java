package org.hage.platform.component.simulationconfig.load;

import org.hage.platform.HageRuntimeException;

public class ConfigurationLoadException extends HageRuntimeException {
    public ConfigurationLoadException(String message) {
        super(message);
    }

    public ConfigurationLoadException(Throwable cause) {
        super(cause);
    }

    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
