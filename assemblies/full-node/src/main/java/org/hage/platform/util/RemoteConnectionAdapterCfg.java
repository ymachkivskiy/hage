package org.hage.platform.util;

import org.hage.platform.util.connection.remote.endpoint.EndpointInitializationPostProcessorBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteConnectionAdapterCfg {

    @Bean
    public EndpointInitializationPostProcessorBean remoteEndpointInitializationBean() {
        return new EndpointInitializationPostProcessorBean();
    }

}
