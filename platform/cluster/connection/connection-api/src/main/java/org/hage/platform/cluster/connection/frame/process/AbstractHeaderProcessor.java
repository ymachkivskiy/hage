package org.hage.platform.cluster.connection.frame.process;

import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Header.HeaderBuilder;

abstract class AbstractHeaderProcessor implements FrameProcessor {

    @Override
    public final Frame process(Frame input) {

        HeaderBuilder mutableHeader = input.getHeader().mutableHeader();

        updateHeader(mutableHeader);

        return new Frame(mutableHeader.build(), input.getPayload());
    }

    protected abstract void updateHeader(HeaderBuilder mutableHeader);

}
