package org.hage.platform;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.*;
import org.hage.platform.component.execution.ExecutionCoreCfg;
import org.hage.platform.util.ContainerShareCfg;
import org.hage.platform.util.EventBusCfg;
import org.hage.platform.util.ExecutorsCfg;
import org.hage.platform.util.RemoteConnectionAdapterCfg;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    AppBaseCfg.class,
    EventBusCfg.class,
    RemoteConnectionAdapterCfg.class,
    ContainerShareCfg.class,
    ExecutorsCfg.class,
    LifecycleCoreCfg.class,
    RateCoreCfg.class,
    RuntimeCoreCfg.class,
    ExecutionCoreCfg.class,
    SimulationConfigurationCoreCfg.class,
    StructureCoreCfg.class,
    SynchronizationCfg.class
})
@ComponentScan(
    basePackageClasses = PlatformCoreCfg.class,
    useDefaultFilters = false,
    includeFilters = {
        @Filter(
            type = FilterType.ANNOTATION,
            classes = {
                PrototypeComponent.class,
                SingletonComponent.class,
                PlugableConfiguration.class
            })
    }
)
public class PlatformCoreCfg {

}
