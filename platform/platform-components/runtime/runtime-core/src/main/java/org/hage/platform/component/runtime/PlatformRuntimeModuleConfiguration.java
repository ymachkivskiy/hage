package org.hage.platform.component.runtime;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformRuntimeModuleConfiguration.class)
class PlatformRuntimeModuleConfiguration {
}
