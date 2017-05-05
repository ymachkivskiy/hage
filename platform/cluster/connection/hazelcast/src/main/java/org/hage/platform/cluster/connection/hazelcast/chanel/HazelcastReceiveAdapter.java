package org.hage.platform.cluster.connection.hazelcast.chanel;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.connection.frame.diagnostics.Diagnostics;
import org.hage.platform.cluster.connection.frame.process.ResponsivenessProcessor;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.chanel.FrameReceiverAdapter;
import org.hage.platform.cluster.connection.chanel.Receiver;
import org.hage.platform.cluster.connection.chanel.RespondReceiver;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Result;
import org.hage.platform.cluster.connection.frame.diagnostics.ResultType;
import org.hage.platform.util.executors.simple.WorkerExecutor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkArgument;
import static lombok.AccessLevel.PACKAGE;
import static org.hage.platform.cluster.connection.frame.process.ConversationIdProcessor.sameConversation;
import static org.hage.platform.cluster.connection.frame.process.DiagnosticsProcessor.successful;
import static org.hage.platform.cluster.connection.frame.process.DiagnosticsProcessor.withDiagnostics;
import static org.hage.platform.cluster.connection.frame.process.IncludeSenderProcessor.includingSender;
import static org.hage.platform.cluster.connection.frame.process.PayloadDataProcessor.withData;
import static org.hage.platform.cluster.connection.frame.process.PayloadDataProcessor.withoutData;
import static org.hage.platform.cluster.connection.frame.process.ReceiversProcessor.responseFor;
import static org.hage.platform.cluster.connection.frame.util.ResultUtil.isSuccessful;

@RequiredArgsConstructor
@Slf4j
class HazelcastReceiveAdapter implements Receiver, FrameReceiverAdapter {

    private final ConnectionDescriptor descriptor;

    @Setter(PACKAGE)
    private HazelcastSender hazelcastSender;
    @Setter(PACKAGE)
    private WorkerExecutor executor;

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
        executor.submit(() -> processFrame(frame));
    }

    private void processFrame(Frame frame) {

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

    public static Frame successfulResponse(Frame originalFrame, Serializable responseData) {
        checkArgument(originalFrame.getPayload().checkDataType(responseData.getClass()), "Frame %s is not compatible with payload data %s", originalFrame, responseData.getClass());

        return createFrame(
            responseFor(originalFrame),
            includingSender(),
            ResponsivenessProcessor.requiresNoResponse(),
            withData(responseData),
            sameConversation(originalFrame),
            successful()
        );
    }

    public static Frame failedResponseWithDiagnostics(Frame originalFrame, Diagnostics diagnostics) {
        return createFrame(
            responseFor(originalFrame),
            includingSender(),
            ResponsivenessProcessor.requiresNoResponse(),
            withoutData(),
            sameConversation(originalFrame),
            withDiagnostics(diagnostics));
    }

}
