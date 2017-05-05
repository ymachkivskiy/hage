package org.hage.platform.cluster.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Payload;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class PayloadDataProcessor implements FrameProcessor {

    private final Serializable data;

    @Override
    public Frame process(Frame input) {
        log.trace("Set data  to {} for frame {}",data, input);

        return new Frame(input.getHeader(), new Payload(data));
    }

    public static PayloadDataProcessor withData(Serializable data) {
        return new PayloadDataProcessor(data);
    }

    public static PayloadDataProcessor withoutData() {
        return new PayloadDataProcessor(null);
    }

}
