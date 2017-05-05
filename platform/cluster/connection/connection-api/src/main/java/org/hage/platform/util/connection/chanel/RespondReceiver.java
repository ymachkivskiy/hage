package org.hage.platform.util.connection.chanel;

import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Result;

public interface RespondReceiver {
    Result receiveRespond(Frame frame);
}
