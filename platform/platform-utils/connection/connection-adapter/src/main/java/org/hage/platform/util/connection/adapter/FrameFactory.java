package org.hage.platform.util.connection.adapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.data.Payload;
import org.hage.platform.util.connection.data.frame.Frame;
import org.hage.platform.util.connection.data.frame.MulticastFrame;
import org.hage.platform.util.connection.data.frame.UnicastFrame;
import org.hage.platform.util.connection.data.header.MulticastHeader;
import org.hage.platform.util.connection.data.header.UnicastHeader;

@RequiredArgsConstructor
public class FrameFactory<T> {

    private final Class<T> payloadClazz;

    public T getData(Frame frame) {
        return frame.getPayload().getData(payloadClazz);
    }

    public UnicastFrame newFrame(UnicastHeader header, T data) {
        return new UnicastFrame(header, new Payload(data));
    }

    public MulticastFrame newFrame(MulticastHeader header, T data) {
        return new MulticastFrame(header, new Payload(data));
    }

}
