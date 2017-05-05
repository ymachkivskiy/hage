package org.hage.platform.cluster.connection.chanel;

import org.hage.platform.cluster.connection.frame.Frame;

public interface FrameSender {
    void send(Frame frame);
}
