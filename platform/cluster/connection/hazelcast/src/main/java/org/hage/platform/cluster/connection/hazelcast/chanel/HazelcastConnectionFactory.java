package org.hage.platform.cluster.connection.hazelcast.chanel;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.cluster.connection.chanel.ConnectionFactory;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.chanel.FrameReceiverAdapter;
import org.hage.platform.cluster.connection.chanel.FrameSender;
import org.hage.platform.cluster.connection.hazelcast.HazelcastInstanceHolder;
import org.hage.platform.util.executors.simple.WorkerExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.cache.CacheBuilder.newBuilder;

public class HazelcastConnectionFactory implements ConnectionFactory {

    @Autowired
    private HazelcastInstanceHolder hazelcastInstanceHolder;
    @Autowired
    private LocalClusterNode localClusterNode;
    @Autowired
    private WorkerExecutor executor;

    private final LoadingCache<ConnectionDescriptor, ChanelPair> cache = newBuilder().build(new Loader());

    @Override
    public FrameSender senderFor(ConnectionDescriptor descriptor) {
        return cache.getUnchecked(descriptor).sender;
    }

    @Override
    public FrameReceiverAdapter receiverAdapterFor(ConnectionDescriptor descriptor) {
        return cache.getUnchecked(descriptor).receiveAdapter;
    }

    @Data
    private static class ChanelPair {
        private final HazelcastReceiveAdapter receiveAdapter;
        private final HazelcastSender sender;
    }

    private class Loader extends CacheLoader<ConnectionDescriptor, ChanelPair> {

        @Override
        public ChanelPair load(ConnectionDescriptor descriptor) throws Exception {

            HazelcastReceiveAdapter receiveAdapter = new HazelcastReceiveAdapter(descriptor);
            HazelcastSender sender = new HazelcastSender(descriptor, localClusterNode, hazelcastInstanceHolder.getInstance());

            receiveAdapter.setHazelcastSender(sender);
            receiveAdapter.setExecutor(executor);
            sender.setReceiver(receiveAdapter);

            sender.initialize();

            return new ChanelPair(receiveAdapter, sender);
        }
    }
}
