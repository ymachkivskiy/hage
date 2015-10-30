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
 * Created: 2012-08-21
 * $Id$
 */

package org.jage.lifecycle;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.jage.lifecycle.LifecycleMessages.createExit;
import static org.jage.lifecycle.LifecycleMessages.createStart;
import static org.junit.Assert.assertThat;


/**
 * Tests for the {@link LifecycleMessages} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class LifecycleMessagesTest {

    @Test
    public void testCreateStart() {
        // when
        final LifecycleMessage message = createStart();

        // then
        assertThat(message, is(notNullValue()));
        assertThat(message.getCommand(), is(LifecycleMessage.LifecycleCommand.START));
        assertThat(message.getPayload(), is(nullValue()));
    }

    @Test
    public void testCreateExit() {
        // when
        final LifecycleMessage message = createExit();

        // then
        assertThat(message, is(notNullValue()));
        assertThat(message.getCommand(), is(LifecycleMessage.LifecycleCommand.EXIT));
        assertThat(message.getPayload(), is(nullValue()));
    }
}
