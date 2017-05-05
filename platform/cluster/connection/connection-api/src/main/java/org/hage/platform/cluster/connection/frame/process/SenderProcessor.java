package org.hage.platform.cluster.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.connection.frame.Header.HeaderBuilder;

@Slf4j
@RequiredArgsConstructor
public class SenderProcessor extends AbstractHeaderProcessor {
    private final NodeAddress sender;

    @Override
    protected void updateHeader(HeaderBuilder mutableHeader) {
        log.trace("Set sender for mutable header {} to {}", mutableHeader, sender);

        mutableHeader.sender(sender);
    }
}
