package org.hage.platform.util.connection.hazelcast.chanel;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.FrameReceiverAdapter;
import org.hage.platform.util.connection.chanel.Receiver;
import org.hage.platform.util.connection.chanel.RespondReceiver;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Result;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;
import org.hage.platform.util.connection.frame.diagnostics.ResultType;

import java.util.concurrent.atomic.AtomicReference;

import static lombok.AccessLevel.PACKAGE;
import static org.hage.platform.util.connection.frame.util.FrameCreators.failedResponseWithDiagnostics;
import static org.hage.platform.util.connection.frame.util.FrameCreators.successfulResponse;
import static org.hage.platform.util.connection.frame.util.FrameUtil.requiresResponse;
import static org.hage.platform.util.connection.frame.util.FrameUtil.successFrame;
import static org.hage.platform.util.connection.frame.util.ResultUtil.isSuccessful;

@RequiredArgsConstructor
@Slf4j
class HazelcastReceiveAdapter implements Receiver, FrameReceiverAdapter {

    private final ConnectionDescriptor descriptor;

    @Setter(PACKAGE)
    private HazelcastSender hazelcastSender;

    private AtomicReference<Receiver> receiverHolder = new AtomicReference<>();
    private AtomicReference<RespondReceiver> respondReceiverHolder = new AtomicReference<>();

    @Override
    public void setReceiver(Receiver receiver) {
        if (!receiverHolder.compareAndSet(null, receiver)) {
            log.warn("Failed to set new receiver for chanel {}, current is {}", descriptor, receiverHolder.get());
        }
    }

    @Override
    public void setRespondReceiver(RespondReceiver respondReceiver) {
        if (!respondReceiverHolder.compareAndSet(null, respondReceiver)) {
            log.warn("Failed to set new respond receiver for chanel {}, current is {}", descriptor, respondReceiverHolder.get());
        }
    }

    @Override
    public void receive(Frame frame) {
        log.debug("Frame received {} with chanel {}", frame, descriptor);

        if (successFrame(frame)) {

            if (requiresResponse(frame)) {
                consumeFrameAndRespond(frame);
            } else {
                consumeFrameWithoutResponse(frame);
            }

        } else {
            log.error("Error frame received {} with chanel {}", frame, descriptor);
        }

    }

    private void consumeFrameAndRespond(Frame frame) {
        RespondReceiver respondReceiver = respondReceiverHolder.get();

        if (respondReceiver == null) {
            log.warn("Received frame {} requires response, but no respond receiver is registered for chanel {}", frame, descriptor);
            hazelcastSender.send(failedResponseWithDiagnostics(frame, new Diagnostics(ResultType.ERROR, "No response receiver registered")));
        } else {
            Result result = respondReceiver.receiveRespond(frame);
            log.debug("Response result for frame {} is {}", frame, result);

            if (isSuccessful(result)) {
                hazelcastSender.send(successfulResponse(frame, result.getData()));
            } else {
                hazelcastSender.send(failedResponseWithDiagnostics(frame, result.getDiagnostics()));
            }
        }
    }

    private void consumeFrameWithoutResponse(Frame frame) {
        Receiver receiver = receiverHolder.get();

        if (receiver == null) {
            log.warn("Received frame {}, but no receiver registered for chanel {}", frame, descriptor);
        } else {
            receiver.receive(frame);
        }
    }

}
