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
 * Created: 2009-04-20
 * $Id$
 */

package org.hage.platform.component.definition;


import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ValueDefinitionTest {

    private final Class<String> desiredClass = String.class;

    private final String stringValue = "value";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnNullDesiredClass() {
        // when
        new ValueDefinition(null, null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDesiredClass() {
        // given
        ValueDefinition definition = new ValueDefinition(desiredClass, stringValue);

        // then
        assertThat(definition.getDesiredClass(), is(equalTo(desiredClass)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnNullStringValue() {
        // when
        new ValueDefinition(desiredClass, null);
    }

    @Test
    public void testStringValue() {
        // given
        ValueDefinition definition = new ValueDefinition(desiredClass, stringValue);

        // then
        assertThat(definition.getStringValue(), is(equalTo(stringValue)));
    }
}
