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
 * Created: 2012-03-04
 * $Id$
 */

package org.hage.communication.message;


import org.hage.address.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link Messages} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class MessagesTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testGetPayloadOfTypeOrThrowWithCorrectType() {
        // given
        String samplePayload = "payload";
        SimpleMessage<Address, String> message = SimpleMessage.create(mock(Header.class), samplePayload);

        // when
        String returnedPayload = Messages.getPayloadOfTypeOrThrow(message, String.class);

        // then
        assertThat(returnedPayload, is(equalTo(samplePayload)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testGetPayloadOfTypeOrThrowWithWrongType() {
        // given
        String samplePayload = "payload";
        SimpleMessage<Address, Serializable> message = SimpleMessage.create(mock(Header.class), samplePayload);

        // when
        Messages.getPayloadOfTypeOrThrow(message, Integer.class);

        // then should throw
    }

}
