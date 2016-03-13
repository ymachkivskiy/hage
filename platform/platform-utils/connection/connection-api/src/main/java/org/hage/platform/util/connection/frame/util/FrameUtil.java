package org.hage.platform.util.connection.frame.util;

import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.frame.Frame;

import java.util.Collection;

import static org.hage.platform.util.connection.frame.AddressingType.BROADCAST;
import static org.hage.platform.util.connection.frame.Responsiveness.REQUIRES_RESPONSE;
import static org.hage.platform.util.connection.frame.diagnostics.ResultType.SUCCESS;

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

    public static Collection<NodeAddress> getReceiverAdresses(Frame frame) {
        return frame.getHeader().getReceivers();
    }

}
