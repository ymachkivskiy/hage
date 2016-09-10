package org.hage.platform.component.synchronization;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.ClusterManager;
import org.hage.platform.component.cluster.ClusterMemberChangeCallback;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.cluster.OrderedClusterMembersStepView;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.hage.platform.component.synchronization.SynchPoint.pointForName;
import static org.hage.util.concurrency.Locks.withExceptionLock;
import static org.hage.util.concurrency.Locks.withLock;

@SingletonComponent
@Slf4j
class SynchronizationEndpoint extends BaseRemoteEndpoint<SynchronizationMessage> implements SynchronizationBarrier, ClusterMemberChangeCallback {

    private static final String CHANEL_NAME = "synchronization-remote-chanel";

    @Autowired
    private OrderedClusterMembersStepView clusterMembersStepView;

    private final Map<SynchPoint, Integer> stepPhaseParkingMap = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition changeNotifier = lock.newCondition();

    private boolean clusterFailed = false;

    public SynchronizationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), SynchronizationMessage.class);
    }

    @Autowired
    private void setClusterManager(ClusterManager clusterManager) {
        clusterManager.addMembershipChangeCallback(this);
    }

    @Override
    public void synchronize(SynchPoint point) {

        log.debug("Start synchronization");

        sendToAll(toMessage(point));
        waitForAll(point);

        log.debug("Finish synchronization");
    }

    @Override
    protected void consumeMessage(MessageEnvelope<SynchronizationMessage> envelope) {
        withLock(lock,
            () -> {
                log.debug("Node {} has reached synchronization point {}.", envelope.getOrigin(), envelope.getBody().getSynchronizationPoint());

                stepPhaseParkingMap.merge(toPoint(envelope.getBody()), 1, Math::addExact);
                changeNotifier.signal();
            }
        );
    }

    @Override
    public void onMemberRemoved(NodeAddress removedMember) {
        withLock(lock,
            () -> {
                clusterFailed = true;
                changeNotifier.signalAll();
            }
        );
    }

    private void waitForAll(SynchPoint point) {
        withExceptionLock(lock,
            () -> {
                while (!clusterFailed && allHaveArrivedTo(point)) {
                    changeNotifier.await();
                }

                stepPhaseParkingMap.remove(point);
            }
        );
    }

    private boolean allHaveArrivedTo(SynchPoint point) {
        return stepPhaseParkingMap.getOrDefault(point, 0) != clusterMembersStepView.membersCount();
    }

    private static SynchronizationMessage toMessage(SynchPoint point) {
        return new SynchronizationMessage(point.getName());
    }

    private static SynchPoint toPoint(SynchronizationMessage message) {
        return pointForName(message.getSynchronizationPoint());
    }
}
