package org.hage.platform.util.connection.chanel;

import org.hage.platform.util.connection.frame.Frame;

public interface Receiver {
    void receive(Frame frame);
}
