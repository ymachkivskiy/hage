package org.jage.performance.node.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CategoryConfiguration {
    @Getter
    private final int categoryBaseWeight;
    @Getter
    private final int categoryWeight;
}
