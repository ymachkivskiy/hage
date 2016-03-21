package org.hage.platform.util.connection.remote.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.util.connection.chanel.Receiver;
import org.hage.platform.util.connection.chanel.RespondReceiver;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Result;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

import java.io.Serializable;

import static org.hage.platform.util.connection.frame.diagnostics.Diagnostics.SUCCESS_DIAGNOSTICS;
import static org.hage.platform.util.connection.frame.diagnostics.ResultType.ERROR;
import static org.hage.platform.util.connection.frame.util.FrameUtil.*;

@Slf4j
@ToString
@RequiredArgsConstructor
class FrameReceiverMessageAdapter<M extends Serializable> implements Receiver, RespondReceiver {

    private final BaseRemoteEndpoint<M> endpoint;

    @Override
    public void receive(Frame frame) {
        log.trace("Received frame {}", frame);

        MessageEnvelope<M> message = translateToRemoteMessage(frame);

        if (belongsToConversation(frame)) {
            endpoint.consumeResponseMessageForConversation(message, getConversationIdOf(frame));
        } else {
            endpoint.consumeMessage(message);
        }

    }

    @Override
    public Result receiveRespond(Frame frame) {
        log.trace("Received frame {}", frame);

        M respond;
        Diagnostics diagnostics;

        try {
            MessageEnvelope<M> messageEnvelope = translateToRemoteMessage(frame);
            respond = endpoint.consumeMessageAndRespond(messageEnvelope);
            diagnostics = SUCCESS_DIAGNOSTICS;
        } catch (Exception e) {
            e.printStackTrace();
            respond = null;
            diagnostics = new Diagnostics(ERROR, e.getMessage());
        }

        return new Result(respond, diagnostics);
    }

    private MessageEnvelope<M> translateToRemoteMessage(Frame frame) {
        Class<M> messageClazz = endpoint.getMessageClazz();
        log.trace("Getting payload data using data class '{}' declared by subscriber '{}'", messageClazz, endpoint.getClass());

        NodeAddress sender = getFrameSenderAddress(frame);
        M message = frame.getPayload().getData(messageClazz);
        log.debug("Get remote message '{}' from node '{}'", message, sender);

        return new MessageEnvelope<>(sender, message);
    }

}
