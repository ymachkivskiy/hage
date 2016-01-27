package org.hage.platform;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(
        basePackageClasses = PlatformConfiguration.class,
        useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(Configuration.class)})
@PropertySource("classpath:application.properties")
public class PlatformConfiguration {

    @Bean
    AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
        return new AutowiredAnnotationBeanPostProcessor();
    }

}
