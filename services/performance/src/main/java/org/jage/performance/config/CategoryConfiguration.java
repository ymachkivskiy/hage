package org.jage.performance.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CategoryConfiguration {
    @Getter
    private final int categoryBaseWeight;
    @Getter
    private final int categoryWeight;
}
