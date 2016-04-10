package org.hage.platform.util.connection.remote;

import org.hage.platform.util.connection.remote.config.RemoteConnectionConfigurationProvider;
import org.hage.platform.util.connection.remote.endpoint.EndpointInitializationPostProcessorBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformUtilConnectionRemoteAdapterModuleConfiguration {

    @Bean
    public EndpointInitializationPostProcessorBean remoteEndpointInitializationBean() {
        return new EndpointInitializationPostProcessorBean();
    }

}
