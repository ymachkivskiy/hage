package org.hage.platform.util.communication;

import org.hage.platform.util.communication.hazelcast.HazelcastRemoteCommunicationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformConnectionLinkModuleConfiguration {

    @Bean
    public HazelcastRemoteCommunicationManager remoteCommunicationManager() {
        return new HazelcastRemoteCommunicationManager();
    }

}
