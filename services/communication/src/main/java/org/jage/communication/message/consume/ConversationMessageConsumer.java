package org.jage.communication.message.consume;


import org.jage.communication.message.ServiceMessage;

import java.util.Optional;
import java.util.Set;

import static java.util.Optional.ofNullable;

public abstract class ConversationMessageConsumer<RemoteMessageT extends ServiceMessage>
        extends BaseConditionalMessageConsumer<RemoteMessageT> {

    private Optional<Set<Long>> trackedConversationIds;

    protected ConversationMessageConsumer() {
        this(null);
    }

    protected ConversationMessageConsumer(Set<Long> trackedConversationIds) {
        this.trackedConversationIds = ofNullable(trackedConversationIds);
    }

    @Override
    public final boolean messageMatches(RemoteMessageT remoteMessage) {
        return ofNullable(remoteMessage.getHeader().getConversationId())
                .map(this::isTrackingConversation)
                .orElse(true)
                &&
                messageMatchAdditionalCheck(remoteMessage);
    }

    private boolean isTrackingConversation(Long conversationId) {
        return trackedConversationIds
                .map(conversations -> conversations.contains(conversationId))
                .orElse(true);
    }

    protected boolean messageMatchAdditionalCheck(RemoteMessageT remoteMessage) {
        return true;
    }
}
