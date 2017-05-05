package org.hage.platform.cluster.connection.remote.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.LocalClusterNode;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.connection.chanel.Receiver;
import org.hage.platform.cluster.connection.chanel.RespondReceiver;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.diagnostics.Diagnostics;
import org.hage.platform.cluster.connection.frame.util.FrameUtil;
import org.hage.platform.cluster.connection.frame.Result;

import java.io.Serializable;

import static org.hage.platform.cluster.connection.frame.diagnostics.ResultType.ERROR;

@Slf4j
@ToString
@RequiredArgsConstructor
class FrameReceiverMessageAdapter<M extends Serializable> implements Receiver, RespondReceiver {

    private final BaseRemoteEndpoint<M> endpoint;
    private final LocalClusterNode localClusterNode;

    @Override
    public void receive(Frame frame) {
        log.trace("Received frame {}", frame);

        MessageEnvelope<M> message = translateToRemoteMessage(frame);

        if (FrameUtil.belongsToConversation(frame)) {
            endpoint.consumeResponseMessageForConversation(message, FrameUtil.getConversationIdOf(frame));
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
            diagnostics = Diagnostics.SUCCESS_DIAGNOSTICS;
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

        NodeAddress sender = FrameUtil.getFrameSenderAddress(frame);
        M message = frame.getPayload().getData(messageClazz);
        log.debug("Get remote message '{}' from node '{}'", message, sender);

        return new MessageEnvelope<>(sender, message, localClusterNode.getLocalAddress().equals(sender));
    }

}
