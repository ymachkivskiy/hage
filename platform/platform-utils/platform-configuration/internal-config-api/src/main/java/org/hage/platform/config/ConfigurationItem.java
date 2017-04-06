package org.hage.platform.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@ToString
@EqualsAndHashCode(of = "itemId")
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

    public abstract void checkValue(Object value) throws ConfigurationValueCheckException;

}
