package org.hage.platform.util.connection.data.frame;

import lombok.Getter;
import org.hage.platform.util.connection.data.Payload;
import org.hage.platform.util.connection.data.header.MulticastHeader;

public class MulticastFrame extends Frame {

    @Getter
    private final MulticastHeader multicastHeader;

    public MulticastFrame(MulticastHeader header, Payload payload) {
        super(header, payload);
        this.multicastHeader = header;
    }

}
