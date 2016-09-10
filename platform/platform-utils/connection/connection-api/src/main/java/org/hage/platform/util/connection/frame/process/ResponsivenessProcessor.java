package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.frame.Header;
import org.hage.platform.util.connection.frame.Responsiveness;

import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.util.connection.frame.Responsiveness.REQUIRES_RESPONSE;
import static org.hage.platform.util.connection.frame.Responsiveness.WITHOUT_RESPONSE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ResponsivenessProcessor extends AbstractHeaderProcessor {

    private final Responsiveness responsiveness;

    @Override
    protected void updateHeader(Header.HeaderBuilder mutableHeader) {
        log.trace("Set responsiveness for header {} to {}", mutableHeader, responsiveness);

        mutableHeader.responsiveness(responsiveness);
    }

    public static ResponsivenessProcessor requiresNoResponse() {
        return new ResponsivenessProcessor(WITHOUT_RESPONSE);
    }

    public static ResponsivenessProcessor requiresResponse() {
        return new ResponsivenessProcessor(REQUIRES_RESPONSE);
    }

}
