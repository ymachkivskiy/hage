package org.hage.platform.util.connection.remote.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.chanel.Receiver;
import org.hage.platform.util.connection.chanel.RespondReceiver;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Result;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

import java.io.Serializable;

import static org.hage.platform.util.connection.frame.diagnostics.Diagnostics.SUCCESS_DIAGNOSTICS;
import static org.hage.platform.util.connection.frame.diagnostics.ResultType.ERROR;
import static org.hage.platform.util.connection.frame.util.FrameUtil.getFrameSender;

@Slf4j
@ToString
@RequiredArgsConstructor
class FrameReceiverMessageTranslationAdapter<M extends Serializable, R extends Serializable> implements Receiver, RespondReceiver {

    private final BaseRemoteMessageSubscriber<M, R> subscriberDelegate;

    @Override
    public void receive(Frame frame) {
        log.trace("Received frame {}", frame);
        subscriberDelegate.consume(translateToRemoteMessage(frame));
    }

    @Override
    public Result receiveRespond(Frame frame) {
        log.trace("Received frame {}", frame);

        R respond;
        Diagnostics diagnostics;

        try {
            RemoteMessage<M> remoteMessage = translateToRemoteMessage(frame);
            respond = subscriberDelegate.consumeAndRespond(remoteMessage);
            diagnostics = SUCCESS_DIAGNOSTICS;
        } catch (Exception e) {
            e.printStackTrace();
            respond = null;
            diagnostics = new Diagnostics(ERROR, e.getMessage());
        }

        return new Result(respond, diagnostics);
    }

    private RemoteMessage<M> translateToRemoteMessage(Frame frame) {
        Class<M> messageClazz = subscriberDelegate.getMessageClazz();
        log.trace("Getting payload data using data class '{}' declared by subscriber '{}'", messageClazz, subscriberDelegate.getClass());

        NodeAddress sender = getFrameSender(frame);
        M message = frame.getPayload().getData(messageClazz);
        log.debug("Get remote message '{}' from node '{}'", message, sender);

        return new RemoteMessage<>(sender, message);
    }

}
