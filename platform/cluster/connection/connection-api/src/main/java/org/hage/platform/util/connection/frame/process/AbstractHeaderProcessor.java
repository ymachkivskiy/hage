package org.hage.platform.util.connection.frame.process;

import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Header.HeaderBuilder;

abstract class AbstractHeaderProcessor implements FrameProcessor {

    @Override
    public final Frame process(Frame input) {

        HeaderBuilder mutableHeader = input.getHeader().mutableHeader();

        updateHeader(mutableHeader);

        return new Frame(mutableHeader.build(), input.getPayload());
    }

    protected abstract void updateHeader(HeaderBuilder mutableHeader);

}
