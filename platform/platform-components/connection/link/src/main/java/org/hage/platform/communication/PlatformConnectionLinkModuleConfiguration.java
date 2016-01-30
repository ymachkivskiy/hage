package org.hage.platform.communication;

import org.hage.platform.communication.hazelcast.HazelcastRemoteCommunicationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformConnectionLinkModuleConfiguration {

    @Bean
    public HazelcastRemoteCommunicationManager remoteCommunicationManager() {
        return new HazelcastRemoteCommunicationManager();
    }

}
