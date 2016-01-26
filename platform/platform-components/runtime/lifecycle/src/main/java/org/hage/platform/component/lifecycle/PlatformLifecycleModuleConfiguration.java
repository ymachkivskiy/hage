package org.hage.platform.component.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformLifecycleModuleConfiguration.class)
class PlatformLifecycleModuleConfiguration {

    @Bean
    public DefaultLifecycleManager getLifecycleManager() {
        return new DefaultLifecycleManager();
    }

    @Bean
    public AutoExitHook getAutoExitHook() {
        return new AutoExitHook();
    }

}
