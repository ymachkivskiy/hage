package org.hage.platform.util.connection.frame;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.util.connection.frame.diagnostics.Diagnostics;

import java.io.Serializable;
import java.util.Set;

@ToString
@Getter
@Builder
public class Header implements Serializable {

    private final boolean includeSender;
    private final NodeAddress sender;
    private final Long conversationId;
    private final Responsiveness responsiveness;
    private final AddressingType addressingType;
    private final Set<NodeAddress> receivers;
    private final Diagnostics diagnostics;

    public HeaderBuilder mutableHeader() {
        return builder()
            .includeSender(includeSender)
            .sender(sender)
            .conversationId(conversationId)
            .responsiveness(responsiveness)
            .addressingType(addressingType)
            .receivers(receivers)
            .diagnostics(diagnostics);
    }

}
