package org.hage.platform.util.connection.hazelcast;

import com.hazelcast.core.Member;
import lombok.EqualsAndHashCode;
import org.hage.platform.component.cluster.NodeAddress;

import java.net.InetSocketAddress;

@EqualsAndHashCode
public class HazelcastNodeAddress implements NodeAddress {

    private final String uniqueAddress;

    public HazelcastNodeAddress(Member clusterMember) {
        InetSocketAddress memberSocketAddress = clusterMember.getInetSocketAddress();
        this.uniqueAddress = memberSocketAddress.getAddress().getHostAddress() + ":" + memberSocketAddress.getPort();
    }

    @Override
    public String getFriendlyIdentifier() {
        return uniqueAddress;
    }

    @Override
    public String toString() {
        return uniqueAddress;
    }

}
