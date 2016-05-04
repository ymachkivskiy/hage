package org.hage.platform.component.runtime.stopcondition.remote;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.lifecycle.LifecycleCommandInvoker;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.remote.endpoint.BaseRemoteEndpoint;
import org.hage.platform.util.connection.remote.endpoint.MessageEnvelope;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hage.platform.component.lifecycle.BaseLifecycleCommand.STOP;
import static org.hage.platform.component.runtime.stopcondition.remote.StopConditionSatisfiedMessage.ack;
import static org.hage.platform.component.runtime.stopcondition.remote.StopConditionSatisfiedMessage.notification;

@SingletonComponent
@Slf4j
public class StopConditionEndpoint extends BaseRemoteEndpoint<StopConditionSatisfiedMessage> {

    private static final String CHANEL_NAME = "stop-condition-remote-chanel";

    @Autowired
    private LifecycleCommandInvoker lifecycleCommandInvoker;

    protected StopConditionEndpoint() {
        super(new ConnectionDescriptor(CHANEL_NAME), StopConditionSatisfiedMessage.class);
    }

    public void notifyAllStopConditionReached() {
        sendToAllAndAggregateResponse(notification(), l -> "OK");
    }

    @Override
    protected StopConditionSatisfiedMessage consumeMessageAndRespond(MessageEnvelope<StopConditionSatisfiedMessage> envelope) {
        log.debug("Stop condition has been satisfied by node {}", envelope.getOrigin());

        lifecycleCommandInvoker.invokeCommand(STOP);

        return ack();
    }
}
