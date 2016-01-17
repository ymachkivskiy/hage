package org.hage.address;


import org.hage.address.node.NodeAddress;
import org.hage.platform.annotation.ReturnValuesAreNonnullByDefault;

import java.io.Serializable;


/**
 * A generic type of addresses used in AgE.
 * <p>
 * An address consists of three elements:
 * <ul>
 * <li>an identifier;
 * <li>a user friendly name;
 * <li>the address of the node this address was created on.
 * </ul>
 * <p>
 * <p>The identifier and node address together distinguish addresses. The friendly name is not mandatory and should
 * have no impact on addressing logic.
 * <p>
 * <p>Identity should be preserved across serialization (equals, hashcode).
 *
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
public interface Address extends Serializable {

    /**
     * Returns the identifier of this address.
     *
     * @return the identifier.
     */
    String getIdentifier();

    /**
     * Returns the user friendly name of this address.
     *
     * @return the user friendly name
     */
    String getFriendlyName();

    /**
     * Returns the address of the node on which this address was created.
     *
     * @return The node address, not null.
     */
    NodeAddress getNodeAddress();

    /**
     * Provides a string representation of the address, based on the user friendly name.
     *
     * @return a user friendly string representation
     */
    @Override
    String toString();

    /**
     * Provides a string representation of the address, based on the address identifier.
     *
     * @return a identifying string representation.
     */
    String toQualifiedString();
}
