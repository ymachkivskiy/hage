package org.hage.platform.util.connection.adapter;

import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Payload;

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
