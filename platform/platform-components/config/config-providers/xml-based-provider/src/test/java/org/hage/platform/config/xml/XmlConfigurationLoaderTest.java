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

package org.hage.platform.config.xml;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.xml.loaders.DocumentLoader;
import org.hage.platform.config.xml.readers.DocumentReader;

import static org.mockito.Mockito.mock;


/**
 * Unit tests for ConfigurationLoader.
 *
 * @author AGH AgE Team
 */
public class XmlConfigurationLoaderTest {

    private final XmlConfigurationProvider configLoader;
    private DocumentLoader loader;
    private DocumentReader reader;

    public XmlConfigurationLoaderTest() throws ConfigurationException {
        loader = mock(DocumentLoader.class);
        reader = mock(DocumentReader.class);
        configLoader = new XmlConfigurationProvider(loader, reader);
    }

//    @Test
//    public void testWithMocks() throws ConfigurationException {
//        // given
//        String source = "somePath";
//        Document document = mock(Document.class);
//        List<IComponentDefinition> definitions = Collections.emptyList();
//        given(loader.loadDocument(source)).willReturn(document);
//        given(reader.readDocument(document)).willReturn(definitions);
//
//        // when
//        ComputationConfiguration loaded = configLoader.loadConfiguration(new FileConfigurationSource(source));
//
//        // then
//        assertThat(loaded.getComponentsDefinitions(), is(definitions));
//    }
}
