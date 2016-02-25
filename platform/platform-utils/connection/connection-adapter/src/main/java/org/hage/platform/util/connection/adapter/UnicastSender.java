package org.hage.platform.util.connection.adapter;

import org.hage.platform.util.connection.frame.Frame;

public interface UnicastSender {

    Frame sendReceive(UnicastFrame frame);

}
