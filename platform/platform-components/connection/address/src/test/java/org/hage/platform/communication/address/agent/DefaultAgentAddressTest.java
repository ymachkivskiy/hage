/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 29-10-2012
 * $Id$
 */

package org.hage.platform.communication.address.agent;


import org.hage.platform.communication.address.node.UnspecifiedNodeAddress;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for DefaultAgentAddress.
 *
 * @author AGH AgE Team
 */
public class DefaultAgentAddressTest {

    @Test
    public void shouldUseUnspecifiedNodeAddressIfNotProvided() {
        // when
        final DefaultAgentAddress address1 = new DefaultAgentAddress();
        final DefaultAgentAddress address2 = new DefaultAgentAddress("");

        // then
        assertThat(address1.getNodeAddress(), instanceOf(UnspecifiedNodeAddress.class));
        assertThat(address2.getNodeAddress(), instanceOf(UnspecifiedNodeAddress.class));
    }

    @Test
    public void twoInstancesShouldHaveDifferentIdentifiers() {
        // when
        final DefaultAgentAddress address1 = new DefaultAgentAddress();
        final DefaultAgentAddress address2 = new DefaultAgentAddress();

        // then
        assertThat(address1.getIdentifier(), is(Matchers.not(address2.getIdentifier())));
    }

    @Test
    public void twoInstancesShouldNotBeEqual() {
        // when
        final DefaultAgentAddress address1 = new DefaultAgentAddress();
        final DefaultAgentAddress address2 = new DefaultAgentAddress();

        // then
        assertThat(address1, is(not(address2)));
    }

}
