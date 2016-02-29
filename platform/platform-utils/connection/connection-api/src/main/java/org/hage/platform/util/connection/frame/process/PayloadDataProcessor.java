package org.hage.platform.util.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.Payload;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class PayloadDataProcessor implements FrameProcessor {

    private final Serializable data;

    @Override
    public Frame process(Frame input) {
        log.debug("Set data for frame {} to {}", input, data);

        return new Frame(input.getHeader(), new Payload(data));
    }

    public static PayloadDataProcessor withData(Serializable data) {
        return new PayloadDataProcessor(data);
    }

    public static PayloadDataProcessor withoutData() {
        return new PayloadDataProcessor(null);
    }

}
