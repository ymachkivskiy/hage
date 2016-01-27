package org.hage.platform.component;

import org.hage.platform.component.workplace.manager.DefaultWorkplaceManager;
import org.hage.platform.component.workplace.manager.WorkplaceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PlatformRuntimeCoreModuleConfiguration {

    @Bean
    public WorkplaceManager workplaceManager() {
        return new DefaultWorkplaceManager();
    }

}
