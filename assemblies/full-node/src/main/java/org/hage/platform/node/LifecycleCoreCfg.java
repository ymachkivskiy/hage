package org.hage.platform.node;

import org.hage.platform.node.lifecycle.DefaultLifecycleInitializer;
import org.hage.platform.node.lifecycle.LifecycleEngine;
import org.hage.platform.node.lifecycle.LifecycleInitializer;
import org.hage.platform.node.lifecycle.remote.ClusterConsistencyGuard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifecycleCoreCfg {

    @Bean
    public LifecycleEngine getLifecycleEngine() {
        LifecycleEngine lifecycleEngine = new LifecycleEngine();

        lifecycleEngine.setLifecycleInitializer(getLifecycleInitializer());

        return lifecycleEngine;
    }

    @Bean
    public LifecycleInitializer getLifecycleInitializer() {
        return new DefaultLifecycleInitializer();
    }


    @Bean
    public ClusterConsistencyGuard clusterConsistencyGuard() {
        return new ClusterConsistencyGuard();
    }

}
