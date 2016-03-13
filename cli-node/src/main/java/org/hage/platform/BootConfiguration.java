package org.hage.platform;

import org.hage.platform.boot.CommandLineArguments;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(
    basePackageClasses = BootConfiguration.class,
    useDefaultFilters = false,
    includeFilters = {@Filter(Configuration.class)})
@PropertySource("classpath:application.properties")
public class BootConfiguration {

    @Bean
    AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
        return new AutowiredAnnotationBeanPostProcessor();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CommandLineArguments commandLineArgumentsService() {
        return new CommandLineArguments();
    }

}
