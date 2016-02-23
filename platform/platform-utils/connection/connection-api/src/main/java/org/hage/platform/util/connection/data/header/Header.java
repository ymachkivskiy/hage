package org.hage.platform.util.connection.data.header;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.address.NodeAddress;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public abstract class Header implements Serializable {

    private final Long conversationId;
    private final NodeAddress sender;

}
