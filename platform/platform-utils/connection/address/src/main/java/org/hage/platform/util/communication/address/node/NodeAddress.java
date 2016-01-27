package org.hage.platform.util.communication.address.node;


import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;

import java.io.Serializable;


@ReturnValuesAreNonnullByDefault
public interface NodeAddress extends Serializable, Comparable<NodeAddress> {

    String getIdentifier();
}
