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
 * Created: 2012-01-28
 * $Id$
 */

package org.jage.platform.config.xml.readers;


import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jage.platform.component.definition.ConfigurationException;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;


/**
 * Unit tests for MappingDefinitionReader.
 *
 * @author AGH AgE Team
 */
public class MappingDefinitionReaderTest {

    private MappingDefinitionReader<Object> reader;

    @SuppressWarnings("unchecked")
    @Test
    public void testDelegatesToReader() throws ConfigurationException {
        // given
        final String name = "name";
        final Element element = DocumentHelper.createElement(name);
        final Object expected = new Object();
        final IDefinitionReader<Object> mock = Mockito.mock(IDefinitionReader.class);
        given(mock.read(element)).willReturn(expected);
        reader = new MappingDefinitionReader<Object>(Collections.singletonMap(name, mock));

        // when
        final Object read = reader.read(element);

        // then
        assertThat(read, is(expected));
    }

    @Test(expected = ConfigurationException.class)
    public void testThrowsExceptionIfNotFound() throws ConfigurationException {
        // given
        final Element element = DocumentHelper.createElement("any");
        reader = new MappingDefinitionReader<Object>(Collections.<String, IDefinitionReader<Object>> emptyMap());

        // when
        reader.read(element);
    }

}
