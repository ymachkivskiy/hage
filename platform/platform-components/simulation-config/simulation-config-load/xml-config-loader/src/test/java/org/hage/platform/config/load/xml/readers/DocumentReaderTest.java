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

package org.hage.platform.simulationconfig.load.xml.readers;


import org.dom4j.Document;
import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.junit.Test;

import java.util.List;

import static org.dom4j.DocumentHelper.createElement;
import static org.hage.platform.simulationconfig.load.xml.util.DocumentBuilder.emptyDocument;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Unit tests for DocumentReader.
 *
 * @author AGH AgE Team
 */
public class DocumentReaderTest {

    @Test
    public void testDelegates() throws ConfigurationException {
        // given
        final Element firstElement = createElement("first");
        final Element secondElement = createElement("second");

        final IComponentDefinition firstDef = mock(IComponentDefinition.class);
        final IComponentDefinition secondDef = mock(IComponentDefinition.class);

        @SuppressWarnings("unchecked")
        final
        IDefinitionReader<IComponentDefinition> mock = mock(IDefinitionReader.class);
        given(mock.read(firstElement)).willReturn(firstDef);
        given(mock.read(secondElement)).willReturn(secondDef);

        final Document document = emptyDocument().build();
        document.getRootElement().add(firstElement);
        document.getRootElement().add(secondElement);

        final DocumentReader reader = new DocumentReader(mock);

        // when
        final List<IComponentDefinition> list = reader.readDocument(document);

        // then
        assertNotNull(list);
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(firstDef));
        assertThat(list.get(1), is(secondDef));
    }
}
