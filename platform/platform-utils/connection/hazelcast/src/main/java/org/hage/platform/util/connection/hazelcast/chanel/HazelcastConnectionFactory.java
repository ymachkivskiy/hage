package org.hage.platform.util.connection.hazelcast.chanel;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Data;
import org.hage.platform.component.cluster.LocalNodeAddressSupplier;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.ConnectionFactory;
import org.hage.platform.util.connection.chanel.FrameReceiverAdapter;
import org.hage.platform.util.connection.chanel.FrameSender;
import org.hage.platform.util.connection.hazelcast.HazelcastInstanceHolder;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.cache.CacheBuilder.newBuilder;

public class HazelcastConnectionFactory implements ConnectionFactory {

    @Autowired
    private HazelcastInstanceHolder hazelcastInstanceHolder;
    @Autowired
    private LocalNodeAddressSupplier localNodeAddressSupplier;


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
            HazelcastSender sender = new HazelcastSender(descriptor, localNodeAddressSupplier, hazelcastInstanceHolder.getInstance());

            receiveAdapter.setHazelcastSender(sender);
            sender.setReceiver(receiveAdapter);

            sender.initialize();

            return new ChanelPair(receiveAdapter, sender);
        }
    }
}
