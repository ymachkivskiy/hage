package org.hage.platform.cluster.connection.chanel;

import org.hage.platform.cluster.connection.frame.Frame;

public interface Receiver {
    void receive(Frame frame);
}
