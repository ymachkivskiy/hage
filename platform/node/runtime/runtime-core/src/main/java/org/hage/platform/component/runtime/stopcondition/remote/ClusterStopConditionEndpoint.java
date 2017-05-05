package org.hage.platform.component.runtime.stopcondition.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.component.runtime.stopcondition.StopConditionChecker;
import org.hage.platform.component.simulationconfig.Configuration;
import org.hage.platform.component.simulationconfig.CoreConfigurer;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.cluster.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.ASYNC__STOP;
import static org.hage.platform.component.runtime.stopcondition.remote.StopConditionSatisfiedMessage.ack;
import static org.hage.platform.component.runtime.stopcondition.remote.StopConditionSatisfiedMessage.notification;

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
        sendToAllAndAggregateResponse(notification(), l -> "OK");
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
        return ack();
    }

    private void onStopConditionSatisfied(MessageEnvelope<StopConditionSatisfiedMessage> envelope) {
        log.debug("Stop condition has been satisfied by node {}", envelope.getOrigin());
        stopConditionSatisfiedOnRemoteNode.set(true);
        lifecycleCommandInvoker.invokeCommand(ASYNC__STOP);
    }

}
