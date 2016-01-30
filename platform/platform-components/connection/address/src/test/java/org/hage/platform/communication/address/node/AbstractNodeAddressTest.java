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

package org.hage.platform.communication.address.node;


import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author AGH AgE Team
 */
public class AbstractNodeAddressTest {

    @SuppressWarnings("serial")
    @Test
    public void ShouldUseIdentifierInCompareTo() {
        // given
        final String identifier1 = "identifier1";
        final String identifier2 = "identifier2";

        final AbstractNodeAddress address1 = new AbstractNodeAddress() {

            @Override
            public String getIdentifier() {
                return identifier1;
            }

            @Override
            public boolean equals(final Object obj) {
                return false;
            }
        };
        final AbstractNodeAddress address2 = new AbstractNodeAddress() {

            @Override
            public String getIdentifier() {
                return identifier2;
            }

            @Override
            public boolean equals(final Object obj) {
                return false;
            }
        };

        // then
        assertThat(address1.compareTo(address2), is(identifier1.compareTo(identifier2)));
        assertThat(address2.compareTo(address1), is(identifier2.compareTo(identifier1)));
    }

}
