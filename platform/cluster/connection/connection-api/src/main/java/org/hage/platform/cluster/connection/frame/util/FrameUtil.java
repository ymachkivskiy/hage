package org.hage.platform.cluster.connection.frame.util;

import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.connection.frame.process.ProcessorChain;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Header;
import org.hage.platform.cluster.connection.frame.process.FrameProcessor;

import java.util.Set;

import static java.util.Arrays.asList;
import static org.hage.platform.cluster.connection.frame.AddressingType.BROADCAST;
import static org.hage.platform.cluster.connection.frame.Responsiveness.REQUIRES_RESPONSE;
import static org.hage.platform.cluster.connection.frame.diagnostics.ResultType.SUCCESS;

public class FrameUtil {

    public static NodeAddress getFrameSenderAddress(Frame frame) {
        return frame.getHeader().getSender();
    }

    public static boolean requiresResponse(Frame frame) {
        return frame.getHeader().getResponsiveness() == REQUIRES_RESPONSE;
    }

    public static boolean successFrame(Frame frame) {
        return frame.getHeader().getDiagnostics().getResultType() == SUCCESS;
    }

    public static Long getConversationIdOf(Frame frame) {
        return frame.getHeader().getConversationId();
    }

    public static boolean belongsToConversation(Frame frame) {
        return frame.getHeader().getConversationId() != null;
    }

    public static boolean isBroadcastFrame(Frame frame) {
        return frame.getHeader().getAddressingType() == BROADCAST;
    }

    public static boolean isDeliverableToSender(Frame frame) {
        return frame.getHeader().isIncludeSender();
    }

    public static Set<NodeAddress> getReceiverAdresses(Frame frame) {
        return frame.getHeader().getReceivers();
    }

    public static Frame createFrame(FrameProcessor... processors) {
        FrameProcessor processor = new ProcessorChain(asList(processors));
        return processor.process(new Frame(Header.builder().build(), null));
    }
}
