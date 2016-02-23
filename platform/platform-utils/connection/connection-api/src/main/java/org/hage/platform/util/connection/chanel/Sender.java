package org.hage.platform.util.connection.chanel;

import org.hage.platform.util.connection.data.frame.Frame;
import org.hage.platform.util.connection.data.frame.MulticastFrame;
import org.hage.platform.util.connection.data.frame.UnicastFrame;

public interface Sender {

    void send(MulticastFrame frame);

    Frame sendReceive(UnicastFrame frame);
}
