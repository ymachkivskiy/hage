package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Header;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConversationIdProcessor extends AbstractHeaderProcessor {

    private final Long conversationId;

    @Override
    protected void updateHeader(Header.HeaderBuilder mutableHeader) {
        log.debug("Set conversation id for header {} to {}", mutableHeader, conversationId);

        mutableHeader.conversationId(conversationId);
    }

    public static ConversationIdProcessor withoutConversation() {
        return new ConversationIdProcessor(null);
    }

    public static ConversationIdProcessor forConversation(Long conversationId) {
        return new ConversationIdProcessor(conversationId);
    }

    public static ConversationIdProcessor sameConversation(Frame frame) {
        return new ConversationIdProcessor(frame.getHeader().getConversationId());
    }
}
