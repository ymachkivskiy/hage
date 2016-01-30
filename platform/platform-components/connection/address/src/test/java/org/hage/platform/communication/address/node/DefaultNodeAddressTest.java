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
 * Created: 2011-07-18
 * $Id$
 */

package org.hage.platform.communication.address.node;


import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for DefaultNodeAddress.
 *
 * @author AGH AgE Team
 */
public class DefaultNodeAddressTest {

    private final String localPart = "localPart";

    private final String hostname = "hostname";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeAtNullLocalPart() {
        // when
        new DefaultNodeAddress(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeAtNullHostname() {
        // when
        new DefaultNodeAddress("", null);
    }

    @Test
    public void shouldUseDefaultHostname() {
        // when
        final DefaultNodeAddress address = new DefaultNodeAddress(localPart);

        // then
        assertThat(address.getHostname(), is("localhost"));
    }

    @Test
    public void shouldUseGivenLocalPart() {
        // when
        final DefaultNodeAddress address = new DefaultNodeAddress(localPart);

        // then
        assertThat(address.getLocalPart(), is(localPart));
    }

    @Test
    public void shouldUseGivenHostname() {
        // when
        final DefaultNodeAddress address = new DefaultNodeAddress(localPart, hostname);

        // then
        assertThat(address.getHostname(), is(hostname));
    }

    @Test
    public void shouldUseLocalAndHostnameInToString() {
        // when
        final DefaultNodeAddress address = new DefaultNodeAddress(localPart, hostname);

        // then
        assertThat(address, hasToString(localPart + "@" + hostname));
    }

    @Test
    public void shouldUseToStringAsIdentifier() {
        // given
        final DefaultNodeAddress address = new DefaultNodeAddress(localPart, hostname);

        // then
        assertThat(address.getIdentifier(), is(address.toString()));
    }

    @Test
    public void testHashcode() {
        // given
        final String otherLocalPart = "otherLocalPart";
        final String otherHostname = "otherHostname";
        NodeAddress first = new DefaultNodeAddress(localPart, hostname);
        NodeAddress second;

        // same localpart, hostname

        second = new DefaultNodeAddress(localPart, hostname);
        assertThat(first.hashCode(), is(equalTo(second.hashCode())));

        // different localpart
        first = new DefaultNodeAddress(localPart, hostname);
        second = new DefaultNodeAddress(otherLocalPart, hostname);
        assertThat(first.hashCode(), is(not(equalTo(second.hashCode()))));

        // different hostname
        first = new DefaultNodeAddress(localPart, hostname);
        second = new DefaultNodeAddress(localPart, otherHostname);
        assertThat(first.hashCode(), is(not(equalTo(second.hashCode()))));
    }

    @Test
    public void testEquals() {
        // given
        final String otherLocalPart = "otherLocalPart";
        final String otherHostname = "otherHostname";
        NodeAddress first = new DefaultNodeAddress(localPart, hostname);
        NodeAddress second;

        // same localpart, hostname

        second = new DefaultNodeAddress(localPart, hostname);
        assertThat(first, is(equalTo(second)));

        // different localpart
        first = new DefaultNodeAddress(localPart, hostname);
        second = new DefaultNodeAddress(otherLocalPart, hostname);
        assertThat(first, is(not(equalTo(second))));

        // different hostname
        first = new DefaultNodeAddress(localPart, hostname);
        second = new DefaultNodeAddress(localPart, otherHostname);
        assertThat(first, is(not(equalTo(second))));
    }

}
