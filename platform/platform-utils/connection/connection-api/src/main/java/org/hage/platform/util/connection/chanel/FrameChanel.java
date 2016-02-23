package org.hage.platform.util.connection.chanel;

import lombok.Data;

@Data
public class FrameChanel {
    private final Receiver receiver;
    private final Sender sender;
}
