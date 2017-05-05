package org.hage.platform.cluster.connection.chanel;

import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Result;

public interface RespondReceiver {
    Result receiveRespond(Frame frame);
}
