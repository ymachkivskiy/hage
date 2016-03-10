package org.hage.platform.util.connection.hazelcast;

import org.hage.platform.util.connection.ConversationIdProvider;
import org.hage.platform.util.connection.chanel.ConnectionFactory;
import org.hage.platform.util.connection.hazelcast.chanel.HazelcastConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses = PlatformUtilConnectionHazelcastModuleConfiguration.class)
class PlatformUtilConnectionHazelcastModuleConfiguration {

    @Bean
    public ConnectionFactory platformCommunicationUtilChannelFactory() {
        return new HazelcastConnectionFactory();
    }

    @Bean
    public HazelcastClusterAddressManager platformCommunicationUtilClusterAddressManager() {
        return new HazelcastClusterAddressManager();
    }

    @Bean
    public ConversationIdProvider platformCommunicationUtilConversationIdProvider() {
        return new HazelcastConversationIdProvider();
    }

}
