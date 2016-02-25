package org.hage.platform.util.connection.hazelcast;

import com.google.common.base.Supplier;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import org.hage.platform.util.connection.ClusterAddressManager;
import org.hage.platform.util.connection.ClusterMemberChangeCallback;
import org.hage.platform.util.connection.NodeAddress;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Suppliers.memoize;
import static java.util.stream.Collectors.toSet;

public class HazelcastClusterAddressManager implements ClusterAddressManager {

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
            memberChangeCallbacks.forEach(calback -> calback.onMemberRemoved(address));
        }

        private HazelcastNodeAddress getAddress(MembershipEvent membershipEvent) {
            return addressTranslator.translate(membershipEvent.getMember());
        }

    }
}
