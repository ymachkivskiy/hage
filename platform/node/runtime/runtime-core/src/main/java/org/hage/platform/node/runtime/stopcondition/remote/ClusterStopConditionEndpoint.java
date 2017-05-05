package org.hage.platform.node.runtime.stopcondition.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.node.runtime.stopcondition.StopConditionChecker;
import org.hage.platform.simconf.Configuration;
import org.hage.platform.simconf.CoreConfigurer;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.hage.platform.node.lifecycle.BaseLifecycleCommand.ASYNC__STOP;

@SingletonComponent
@Slf4j
class ClusterStopConditionEndpoint extends BaseRemoteEndpoint<StopConditionSatisfiedMessage> implements ClusterStopConditionReachedNotifier, StopConditionChecker, CoreConfigurer {

    private static final String CHANEL_NAME = "stop-condition-remote-chanel";

    private final AtomicBoolean stopConditionSatisfiedOnRemoteNode = new AtomicBoolean(false);

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    protected ClusterStopConditionEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), StopConditionSatisfiedMessage.class);
    }

    @Override
    public void notifyAllStopConditionReached() {
        sendToAllAndAggregateResponse(StopConditionSatisfiedMessage.notification(), l -> "OK");
    }

    @Override
    public boolean isSatisfied() {
        boolean isSatisfied = stopConditionSatisfiedOnRemoteNode.get();
        log.debug("Check if stop condition is satisfied on remote node: {}", isSatisfied);
        return isSatisfied;
    }

    @Override
    public void configureWith(Configuration configuration) {
        log.debug("Reset stop condition is satisfied on remote node");
        stopConditionSatisfiedOnRemoteNode.set(false);
    }

    @Override
    protected void consumeMessage(MessageEnvelope<StopConditionSatisfiedMessage> envelope) {
        onStopConditionSatisfied(envelope);
    }

    @Override
    protected StopConditionSatisfiedMessage consumeMessageAndRespond(MessageEnvelope<StopConditionSatisfiedMessage> envelope) {
        onStopConditionSatisfied(envelope);
        return StopConditionSatisfiedMessage.ack();
    }

    private void onStopConditionSatisfied(MessageEnvelope<StopConditionSatisfiedMessage> envelope) {
        log.debug("Stop condition has been satisfied by node {}", envelope.getOrigin());
        stopConditionSatisfiedOnRemoteNode.set(true);
        lifecycleCommandInvoker.invokeCommand(ASYNC__STOP);
    }

}
