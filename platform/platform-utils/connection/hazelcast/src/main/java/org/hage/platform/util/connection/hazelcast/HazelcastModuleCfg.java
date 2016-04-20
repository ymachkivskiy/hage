package org.hage.platform.util.connection.hazelcast;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.component.cluster.ConversationIdProvider;
import org.hage.platform.util.connection.chanel.ConnectionFactory;
import org.hage.platform.util.connection.hazelcast.chanel.HazelcastConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;


@PlugableConfiguration
@ComponentScan(basePackageClasses = HazelcastModuleCfg.class)
public class HazelcastModuleCfg {

    @Bean
    public ConnectionFactory platformCommunicationUtilChannelFactory() {
        return new HazelcastConnectionFactory();
    }

    @Bean
    @DependsOn("hazelcastInstanceHolder")
    public HazelcastClusterManager platformCommunicationUtilClusterAddressManager() {
        return new HazelcastClusterManager();
    }

    @Bean
    @DependsOn("hazelcastInstanceHolder")
    public ConversationIdProvider platformCommunicationUtilConversationIdProvider() {
        return new HazelcastConversationIdProvider();
    }

    @Bean(name = "hazelcastInstanceHolder")
    public HazelcastInstanceHolder hazelcastInstanceHolder() {
        return new HazelcastInstanceHolder();
    }

}
