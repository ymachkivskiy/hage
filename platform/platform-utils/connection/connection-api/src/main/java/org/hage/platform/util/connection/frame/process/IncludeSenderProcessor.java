package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.frame.Header.HeaderBuilder;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class IncludeSenderProcessor extends AbstractHeaderProcessor {

    private final boolean includeSender;

    @Override
    protected void updateHeader(HeaderBuilder mutableHeader) {
        log.trace("Set include sender to {} for header {}", includeSender, mutableHeader);

        mutableHeader.includeSender(includeSender);
    }

    public static IncludeSenderProcessor includingSender() {
        return includingSender(true);
    }

    public static IncludeSenderProcessor excludingSender() {
        return includingSender(false);
    }

    public static IncludeSenderProcessor includingSender(boolean doInclude) {
        return new IncludeSenderProcessor(doInclude);
    }

}
