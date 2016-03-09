package org.hage.platform.util.connection.frame.util;

import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;
import org.hage.platform.util.connection.frame.process.FrameProcessor;
import org.hage.platform.util.connection.frame.process.ProcessorChain;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Arrays.asList;
import static org.hage.platform.util.connection.frame.process.ConversationIdProcessor.*;
import static org.hage.platform.util.connection.frame.process.DiagnosticsProcessor.successful;
import static org.hage.platform.util.connection.frame.process.DiagnosticsProcessor.withDiagnostics;
import static org.hage.platform.util.connection.frame.process.PayloadDataProcessor.withData;
import static org.hage.platform.util.connection.frame.process.PayloadDataProcessor.withoutData;
import static org.hage.platform.util.connection.frame.process.ReceiversProcessor.*;
import static org.hage.platform.util.connection.frame.process.ResponsivenessProcessor.requiresNoResponse;
import static org.hage.platform.util.connection.frame.process.ResponsivenessProcessor.requiresResponse;

public class FrameCreators {

    public static Frame createWithProcessor(FrameProcessor processor) {
        return processor.process(new Frame(null, null));
    }

    public static Frame successfulResponse(Frame originalFrame, Serializable responseData) {
        checkArgument(originalFrame.getPayload().checkDataType(responseData.getClass()), "Frame %s is not compatible with payload data %s", originalFrame, responseData.getClass());

        FrameProcessor processor = new ProcessorChain(
            asList(
                responseFor(originalFrame),
                requiresNoResponse(),
                withData(responseData),
                sameConversation(originalFrame),
                successful()
            )
        );

        return createWithProcessor(processor);
    }

    public static Frame failedResponseWithDiagnostics(Frame originalFrame, Diagnostics diagnostics) {

        FrameProcessor processor = new ProcessorChain(
            asList(
                responseFor(originalFrame),
                requiresNoResponse(),
                withoutData(),
                sameConversation(originalFrame),
                withDiagnostics(diagnostics)
            )
        );

        return createWithProcessor(processor);
    }

    public static Frame broadcastFrameWithDataNotRequireResponse(Serializable data) {
        FrameProcessor frameProcessor = new ProcessorChain(
            asList(
                broadcast(),
                requiresNoResponse(),
                withData(data),
                withoutConversation(),
                successful()
            )
        );

        return createWithProcessor(frameProcessor);
    }

    public static Frame multicastFrameRequest(Serializable data, Set<NodeAddress> receivers, long conversationId) {
        FrameProcessor frameProcessor = new ProcessorChain(
            asList(
                multicast(receivers),
                requiresResponse(),
                withData(data),
                withConversation(conversationId),
                successful()
            )
        );

        return createWithProcessor(frameProcessor);
    }

    public static Frame multicastFrameWithDataNotRequireResponse(Serializable data, Set<NodeAddress> receivers) {
        FrameProcessor frameProcessor = new ProcessorChain(
            asList(
                multicast(receivers),
                requiresNoResponse(),
                withData(data),
                withoutConversation(),
                successful()
            )
        );

        return createWithProcessor(frameProcessor);
    }


}
