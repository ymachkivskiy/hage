package org.hage.platform.component.synchronization;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.ClusterManager;
import org.hage.platform.component.cluster.ClusterMemberChangeCallback;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SynchronizationEndpoint extends BaseRemoteEndpoint<SynchronizationMessage> implements SynchronizationBarrier, ClusterMemberChangeCallback {

    private static final String CHANEL_NAME = "synchronization-remote-chanel";

    @Autowired
    private ClusterManager clusterManager;

    private final AtomicInteger waitingCount = new AtomicInteger();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition changeNotifier = lock.newCondition();

    public SynchronizationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), SynchronizationMessage.class);
    }

    @Override
    public void synchronize() {

        log.debug("Start synchronization");

        startSynchronization();
        waitForAll();

        log.debug("Finish synchronization");
    }

    @Override
    public void onMemberAdd(NodeAddress newMember) {
        waitingCount.incrementAndGet();
    }

    @Override
    public void onMemberRemoved(NodeAddress removedMember) {
        waitingCount.decrementAndGet();
    }

    @Override
    protected void consumeMessage(MessageEnvelope<SynchronizationMessage> envelope) {
        NodeAddress origin = envelope.getOrigin();
        registerNodeThatReachedBarrier(origin);
    }

    @PostConstruct
    private void init() {
        clusterManager.addMembershipChangeCallback(this);
    }

    private void startSynchronization() {
        waitingCount.set(clusterManager.getMembersCount());
        sendToAll(new SynchronizationMessage());
    }

    private void waitForAll() {
        try {
            lock.lock();

            while (waitingCount.get() > 0) {
                changeNotifier.await();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void registerNodeThatReachedBarrier(NodeAddress origin) {
        try {
            lock.lock();

            log.info("Node {} has reached synchronization point.", origin);

            waitingCount.decrementAndGet();
            changeNotifier.signal();

        } finally {
            lock.unlock();
        }
    }
}
