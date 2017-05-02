package org.hage.platform.config;

import lombok.Data;

@Data
public class ConfigurationItemProperties {
    private final boolean isRequired;
    private final String shortName;
    private final String longName;
    private final String description;
    private final boolean hasValue;
    private final Class<?> valueType;
    private final String valueName;
    private final Object defaultValue;
}
