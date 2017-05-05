package org.hage.platform.cluster.connection.frame.process;

import org.hage.platform.cluster.connection.frame.Frame;

public interface FrameProcessor {

    Frame process(Frame input);

}
