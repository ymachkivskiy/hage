package org.hage.platform.config.distribution.endpoint.message;


import java.io.Serializable;

enum MessageType implements Serializable {
    CHECK_WILL_ACCEPT_CONFIG,
    REFUSE_TO_ACCEPT_CONFIG,
    REQUEST_FOR_CONFIG,
    DISTRIBUTE_CONFIG
}
