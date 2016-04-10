package org.hage.platform.config.parse;

import org.hage.platform.config.ConfigurationCategory;

public interface ParsingEngine {
    ParseResult parse(String[] args);

    void addCategory(ConfigurationCategory category);
}
