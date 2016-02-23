package org.hage.platform.util.connection.data.frame;

import lombok.Getter;
import org.hage.platform.util.connection.data.Payload;
import org.hage.platform.util.connection.data.header.UnicastHeader;

public class UnicastFrame extends Frame {

    @Getter
    private final UnicastHeader unicastHeader;

    public UnicastFrame(UnicastHeader header, Payload payload) {
        super(header, payload);
        this.unicastHeader = header;
    }

}
