package org.hage.platform.component.synchronization;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.ClusterManager;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SynchronizationEndpoint extends BaseRemoteEndpoint<SynchronizationMessage> implements SynchronizationBarrier {

    private static final String CHANEL_NAME = "synchronization-remote-chanel";

    @Autowired
    private ClusterManager clusterManager;

    private final Map<Long, Integer> stepParkedCounter = new ConcurrentHashMap<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition changeNotifier = lock.newCondition();

    public SynchronizationEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), SynchronizationMessage.class);
    }

    @Override
    public void synchronizeOnStep(long stepNumber) {

        log.debug("Start synchronization");

        startSynchronization(stepNumber);
        waitForAll(stepNumber);

        log.debug("Finish synchronization");
    }

    private void startSynchronization(long stepNumber) {
        sendToAll(new SynchronizationMessage(stepNumber));
    }

    @Override
    protected void consumeMessage(MessageEnvelope<SynchronizationMessage> envelope) {
        try {
            lock.lock();

            log.info("Node {} has reached synchronization point.", envelope.getOrigin());

            stepParkedCounter.merge(envelope.getBody().getStepNumber(), 1, Math::addExact);

            changeNotifier.signal();

        } finally {
            lock.unlock();
        }
    }

    private void waitForAll(long stepNumber) {
        try {
            lock.lock();

            while (stepParkedCounter.getOrDefault(stepNumber, 0) != clusterManager.getMembersCount()) {
                changeNotifier.await();
            }

            stepParkedCounter.remove(stepNumber);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
