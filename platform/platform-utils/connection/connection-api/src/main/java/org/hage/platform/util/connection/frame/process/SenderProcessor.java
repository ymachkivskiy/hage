package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.util.connection.frame.Header.HeaderBuilder;

@Slf4j
@RequiredArgsConstructor
public class SenderProcessor extends AbstractHeaderProcessor {
    private final NodeAddress sender;

    @Override
    protected void updateHeader(HeaderBuilder mutableHeader) {
        log.debug("Set sender for mutable header {} to {}", mutableHeader, sender);

        mutableHeader.sender(sender);
    }
}
