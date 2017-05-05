package org.hage.platform.util.connection.hazelcast;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hazelcast.core.Member;

import static com.google.common.cache.CacheBuilder.newBuilder;

public class AddressTranslator {

    private final LoadingCache<Member, HazelcastNodeAddress> nodeAddressLoadingCache = newBuilder()
        .initialCapacity(16)
        .build(new CacheLoader<Member, HazelcastNodeAddress>() {

            @Override
            public HazelcastNodeAddress load(Member key) throws Exception {
                return new HazelcastNodeAddress(key);
            }

        });

    public HazelcastNodeAddress translate(Member member) {
        return nodeAddressLoadingCache.getUnchecked(member);
    }

}
