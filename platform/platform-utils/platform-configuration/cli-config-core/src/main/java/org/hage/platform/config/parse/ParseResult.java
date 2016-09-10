package org.hage.platform.config.parse;

import lombok.Data;
import org.hage.platform.config.ConfigurationItem;

import java.util.Map;

@Data
public class ParseResult {
    private final Map<ConfigurationItem, Object> values;
}
