package org.hage.platform.cluster.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Header;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ConversationIdProcessor extends AbstractHeaderProcessor {

    private final Long conversationId;

    @Override
    protected void updateHeader(Header.HeaderBuilder mutableHeader) {
        log.trace("Set conversation id to {} for header {}", conversationId, mutableHeader);

        mutableHeader.conversationId(conversationId);
    }

    public static ConversationIdProcessor withoutConversation() {
        return new ConversationIdProcessor(null);
    }

    public static ConversationIdProcessor withConversation(Long conversationId) {
        return new ConversationIdProcessor(conversationId);
    }

    public static ConversationIdProcessor sameConversation(Frame frame) {
        return new ConversationIdProcessor(frame.getHeader().getConversationId());
    }
}
