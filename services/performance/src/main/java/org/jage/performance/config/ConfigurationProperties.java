package org.jage.performance.config;

import org.jage.performance.category.PerformanceCategory;

import java.util.EnumMap;
import java.util.Map;

public class ConfigurationProperties {
    private static final int CPU_CATEGORY_BASE_WEIGHT = 5;
    private static final int MEMORY_CATEGORY_BASE_WEIGHT = 4;
    private static final int DISK_CATEGORY_BASE_WEIGHT = 1;

    private static final int CPU_CATEGORY_WEIGHT = 10;
    private static final int DISK_CATEGORY_WEIGHT = 4;
    private static final int MEMORY_CATEGORY_WEIGHT = 4;

    private Map<PerformanceCategory, CategoryConfiguration> categoryConfigurations = new EnumMap<>(PerformanceCategory.class);

    {
        categoryConfigurations.put(PerformanceCategory.CPU, new CategoryConfiguration(CPU_CATEGORY_BASE_WEIGHT, CPU_CATEGORY_WEIGHT));
        categoryConfigurations.put(PerformanceCategory.MEMORY, new CategoryConfiguration(MEMORY_CATEGORY_BASE_WEIGHT, MEMORY_CATEGORY_WEIGHT));
        categoryConfigurations.put(PerformanceCategory.DISK, new CategoryConfiguration(DISK_CATEGORY_BASE_WEIGHT, DISK_CATEGORY_WEIGHT));
    }

    public CategoryConfiguration forCategory(PerformanceCategory category) {
        return categoryConfigurations.get(category);
    }
}
