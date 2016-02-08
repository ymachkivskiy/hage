package org.hage.platform.component.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformLifecycleLocalModuleConfiguration.class)
class PlatformLifecycleLocalModuleConfiguration {

    @Bean
    public LifecycleEngine getLifecycleManager() {
        LifecycleEngine lifecycleEngine = new LifecycleEngine();

        lifecycleEngine.setLifecycleInitializer(getLifecycleInitializer());

        return lifecycleEngine;
    }

    @Bean
    public LifecycleInitializer getLifecycleInitializer() {
        return new DefaultLifecycleInitializer();
    }

}
