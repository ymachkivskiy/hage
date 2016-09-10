package org.hage.platform.config;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@ToString
public abstract class ConfigurationItem {
    private static AtomicInteger idCounter = new AtomicInteger(0);

    @Getter
    private final String itemId;
    @Getter
    private final ConfigurationItemProperties properties;

    protected ConfigurationItem(ConfigurationItemProperties properties) {
        this.properties = properties;
        itemId = "item-" + idCounter.getAndIncrement() + "-" + properties.getShortName();
    }

    public abstract void checkValue(Object defaultValue) throws ConfigurationValueCheckException;


    @Override
    public int hashCode() {
        return itemId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof ConfigurationItem) {
            return ((ConfigurationItem) obj).itemId.equals(itemId);
        }
        return false;
    }
}
