package org.hage.platform.config;

import org.hage.platform.HageRuntimeException;

public class ConfigurationValueCheckException extends HageRuntimeException {
    public ConfigurationValueCheckException(String message) {
        super(message);
    }

    public ConfigurationValueCheckException(Throwable cause) {
        super(cause);
    }

    public ConfigurationValueCheckException(String message, Throwable cause) {
        super(message, cause);
    }
}
