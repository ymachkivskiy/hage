package org.hage.platform.util.connection.frame;

import lombok.Data;

import java.io.Serializable;

@Data
public class Frame implements Serializable {

    private final Header header;
    private final Payload payload;

}
