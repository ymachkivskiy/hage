package org.hage.platform.cluster.connection.hazelcast;

import com.google.common.collect.ImmutableMap;
import com.hazelcast.config.Config;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.connection.config.ConnectionConfiguration;
import org.hage.platform.cluster.connection.config.ConnectionConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

@Slf4j
public class HazelcastInstanceHolder {

    private static final String HAZELCAST_INSTANCE_NAME = "HageHazelcastInstanceName-" + HazelcastInstanceHolder.class.getSimpleName();
    private static final String HAZELCAST_LOGGING_TYPE_PROP = "hazelcast.logging.type";

    @Autowired
    private ConnectionConfigurationProvider configurationProvider;

    @Getter
    private HazelcastInstance instance;

    @PostConstruct
    private void initializeInstance() {
        log.debug("Initializing hazelcast instance");

        Config config = new Config();
        config.setInstanceName(HAZELCAST_INSTANCE_NAME);
        config.setProperty(HAZELCAST_LOGGING_TYPE_PROP, "slf4j");

        updateNetworkConfig(config);
        updateExecutorsConfig(config);

        instance = newHazelcastInstance(config);

        log.debug("Hazelcast instance has been initialized");

    }

    private void updateExecutorsConfig(Config config) {
        // fixme: this is workaround, actual solution not depend on hazelcast threads pool is to  process responses in separate thread
        config.setExecutorConfigs(
            ImmutableMap.of(
                "default",
                new ExecutorConfig("default", 16)
            )
        );
    }

    @PreDestroy
    private void destroyInstance() {
        instance.shutdown();
    }

    private void updateNetworkConfig(Config config) {
        ConnectionConfiguration connectionConfig = configurationProvider.getConfiguration();
        if (connectionConfig.isUseSpecificInterface()) {
            InterfacesConfig iConfig = config.getNetworkConfig().getInterfaces();
            iConfig.setEnabled(true);
            iConfig.addInterface(connectionConfig.getNetworkInterfaceAddress());
        }
    }

}
