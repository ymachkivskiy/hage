package org.hage.platform.util.connection.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

@Component
@Slf4j
public class HazelcastInstanceHolder {

    private static final String HAZELCAST_INSTANCE_NAME = "HageHazelcastInstanceName-" + HazelcastInstanceHolder.class.getSimpleName();
    private static final String HAZELCAST_LOGGING_TYPE_PROP = "hazelcast.logging.type";

    @Getter
    private HazelcastInstance instance;

    public HazelcastInstanceHolder() {
        log.debug("Initializing hazelcast instance");

        initializeInstance();

        log.debug("Hazelcast instance has been initialized");
    }

    private void initializeInstance() {

        Config instanceConfig = new Config();
        instanceConfig.setInstanceName(HAZELCAST_INSTANCE_NAME);
        instanceConfig.setProperty(HAZELCAST_LOGGING_TYPE_PROP, "slf4j");

        instance = newHazelcastInstance(instanceConfig);
    }

    @PreDestroy
    private void destroyInstance() {
        instance.shutdown();
    }

}
