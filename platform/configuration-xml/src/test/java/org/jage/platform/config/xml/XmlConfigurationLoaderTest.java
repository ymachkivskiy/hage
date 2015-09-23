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

package org.jage.platform.config.xml;


import org.dom4j.Document;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.xml.loaders.DocumentLoader;
import org.jage.platform.config.xml.readers.DocumentReader;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Unit tests for ConfigurationLoader.
 *
 * @author AGH AgE Team
 */
public class XmlConfigurationLoaderTest {

    private final XmlConfigurationLoader configLoader;
    private DocumentLoader loader;
    private DocumentReader reader;

    public XmlConfigurationLoaderTest() throws ConfigurationException {
        loader = mock(DocumentLoader.class);
        reader = mock(DocumentReader.class);
        configLoader = new XmlConfigurationLoader(loader, reader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSourceIsString() throws ConfigurationException {
        // given
        Object source = new Object();

        // when
        configLoader.loadConfiguration(source);
    }

    @Test
    public void testWithMocks() throws ConfigurationException {
        // given
        String source = "somePath";
        Document document = mock(Document.class);
        List<IComponentDefinition> definitions = Collections.emptyList();
        given(loader.loadDocument(source)).willReturn(document);
        given(reader.readDocument(document)).willReturn(definitions);

        // when
        List<IComponentDefinition> loaded = configLoader.loadConfiguration(source);

        // then
        assertThat(loaded, is(definitions));
    }
}
