package org.hage.platform.simconf.load;

import org.hage.platform.HageException;

public class ConfigurationNotFoundException extends HageException {

    public ConfigurationNotFoundException(String message) {
        super(message);
    }

    public ConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }
}
