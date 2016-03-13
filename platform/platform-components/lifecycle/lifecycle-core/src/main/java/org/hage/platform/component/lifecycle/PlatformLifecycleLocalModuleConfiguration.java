package org.hage.platform.component.lifecycle;

import org.hage.platform.component.lifecycle.remote.ClusterConsistencyGuard;
import org.hage.platform.component.lifecycle.remote.LifecycleManagerEndpoint;
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

    @Bean
    public LifecycleManagerEndpoint lifecycleManagerRemoteChanel() {
        return new LifecycleManagerEndpoint();
    }

    @Bean
    public ClusterConsistencyGuard clusterConsistencyGuard() {
        return new ClusterConsistencyGuard();
    }

}
