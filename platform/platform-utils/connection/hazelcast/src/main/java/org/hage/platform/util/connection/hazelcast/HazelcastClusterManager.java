package org.hage.platform.util.connection.hazelcast;

import com.google.common.base.Supplier;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import org.hage.platform.component.cluster.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Suppliers.memoize;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class HazelcastClusterManager implements ClusterManager, ClusterAddressManager {

    @Autowired
    private HazelcastInstanceHolder hazelcastInstanceHolder;

    private final Supplier<HazelcastNodeAddress> localAddressSupplier = memoize(this::internalGetLocalAddress);
    private final AddressTranslator addressTranslator = new AddressTranslator();

    private final List<ClusterMemberChangeCallback> memberChangeCallbacks = new ArrayList<>();

    @Override
    public void addMembershipChangeCallback(ClusterMemberChangeCallback callback) {
        this.memberChangeCallbacks.add(callback);
    }

    @Override
    public Set<NodeAddress> getOtherMembersAddresses() {
        final HazelcastNodeAddress localAddress = localAddressSupplier.get();
        return getHazelcastCluster().getMembers().stream()
            .map(addressTranslator::translate)
            .filter(address -> !address.equals(localAddress))
            .collect(toSet());
    }

    @Override
    public NodeAddress getLocalAddress() {
        return localAddressSupplier.get();
    }

    @Override
    public Set<NodeAddress> getAllAddresses() {
        Set<NodeAddress> addresses = new HashSet<>(getOtherMembersAddresses());
        addresses.add(getLocalAddress());
        return addresses;
    }

    @Override
    public List<ClusterMember> getClusterMembers() {
        AtomicInteger counter = new AtomicInteger(1);
        return hazelcastInstanceHolder.getInstance().getCluster().getMembers().stream()
            .sorted((m1, m2) -> m1.getUuid().compareTo(m2.getUuid()))
            .map(m -> new ClusterMember(m.localMember(), addressTranslator.translate(m), counter.getAndIncrement()))
            .collect(toList());
    }

    @Override
    public int getMembersCount() {
        return hazelcastInstanceHolder.getInstance().getCluster().getMembers().size();
    }

    @PostConstruct
    private void initialize() {
        getHazelcastCluster().addMembershipListener(new MembershipCallback());
    }

    private HazelcastNodeAddress internalGetLocalAddress() {
        return addressTranslator.translate(getHazelcastCluster().getLocalMember());
    }

    private Cluster getHazelcastCluster() {
        return hazelcastInstanceHolder.getInstance().getCluster();
    }


    private class MembershipCallback implements MembershipListener {

        @Override
        public void memberAdded(MembershipEvent membershipEvent) {
            HazelcastNodeAddress address = getAddress(membershipEvent);
            memberChangeCallbacks.forEach(callback -> callback.onMemberAdd(address));
        }

        @Override
        public void memberRemoved(MembershipEvent membershipEvent) {
            HazelcastNodeAddress address = getAddress(membershipEvent);
            memberChangeCallbacks.forEach(callback -> callback.onMemberRemoved(address));
        }

        private HazelcastNodeAddress getAddress(MembershipEvent membershipEvent) {
            return addressTranslator.translate(membershipEvent.getMember());
        }

    }
}
