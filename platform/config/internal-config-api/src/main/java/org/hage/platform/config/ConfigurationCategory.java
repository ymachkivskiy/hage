package org.hage.platform.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PRIVATE;
import static org.hage.util.CollectionUtils.nullSafe;

@ToString
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ConfigurationCategory {
    private final String categoryName;
    private final String categoryDescription;
    private final List<ConfigurationItem> configItems;


    public static ConfigurationCategory configurationCategory(String name, String description, List<ConfigurationItem> items) {
        checkArgument(name != null);
        checkArgument(description != null);
        return new ConfigurationCategory(name, description, nullSafe(items));
    }
}
