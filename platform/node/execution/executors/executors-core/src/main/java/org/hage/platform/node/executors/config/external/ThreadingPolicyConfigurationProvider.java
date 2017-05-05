package org.hage.platform.node.executors.config.external;

import org.hage.platform.HageRuntimeException;
import org.hage.platform.config.ConfigurationCategory;
import org.hage.platform.config.ConfigurationCategorySupplier;
import org.hage.platform.config.PlatformConfigurationValueProvider;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyConfiguration;
import org.hage.platform.node.executors.config.internal.ThreadingPolicyInternalConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.ConfigurationCategory.configurationCategory;

public class ThreadingPolicyConfigurationProvider implements ThreadingPolicyInternalConfigurationProvider, ConfigurationCategorySupplier {

    private static final ThreadingPolicyConfigurationItem threadingPolicyConfigurationItem = new ThreadingPolicyConfigurationItem();

    @Autowired
    private PlatformConfigurationValueProvider configurationValueProvider;

    @Override
    public ThreadingPolicyConfiguration getThreadingPolicyConfiguration() {
        return configurationValueProvider.getValueOf(threadingPolicyConfigurationItem, String.class)
            .map(threadingPolicyConfigurationItem::convert)
            .orElseThrow(() -> new HageRuntimeException("No threading policy provided"));
    }

    @Override
    public ConfigurationCategory getConfigurationCategory() {
        return configurationCategory(
            "Node execution",
            "Configuration of internal node execution mechanisms",
            singletonList(threadingPolicyConfigurationItem)
        );
    }

}
