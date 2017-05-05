package org.hage.platform.cluster.connection.hazelcast;

import com.hazelcast.core.IAtomicLong;
import org.hage.platform.cluster.api.ConversationIdProvider;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class HazelcastConversationIdProvider implements ConversationIdProvider {

    private static final String HAZELCAST_CONVERSATION_ID_COUNTER = "hazelcastConversationIdCounter";

    @Autowired
    private HazelcastInstanceHolder hazelcastInstanceHolder;
    private IAtomicLong counter;

    @Override
    public Long nextConversationId() {
        return counter.incrementAndGet();
    }

    @PostConstruct
    private void initialize() {
        counter = hazelcastInstanceHolder.getInstance().getAtomicLong(HAZELCAST_CONVERSATION_ID_COUNTER);
    }



}
