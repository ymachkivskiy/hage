package org.hage.platform.util.connection.data.frame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.data.Payload;
import org.hage.platform.util.connection.data.header.Header;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;


@Getter
@RequiredArgsConstructor(access = PROTECTED)
public abstract class Frame implements Serializable {

    private final Header header;
    private final Payload payload;

}
