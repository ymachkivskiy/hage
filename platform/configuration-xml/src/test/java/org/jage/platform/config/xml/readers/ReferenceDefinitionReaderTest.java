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


import org.dom4j.Element;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.ReferenceDefinition;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.jage.platform.config.xml.util.ElementBuilder.element;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


/**
 * Unit tests for ReferenceDefinitionReader.
 *
 * @author AGH AgE Team
 */
public class ReferenceDefinitionReaderTest {

    private final ReferenceDefinitionReader reader = new ReferenceDefinitionReader();

    @Test
    public void testValidDefinition() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.REFERENCE)
                .withAttribute(ConfigAttributes.TARGET, "targetName")
                .withAttribute(ConfigAttributes.CLASS, "java.lang.Object")
                .build();

        // when
        final ReferenceDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertThat(definition.getTargetName(), is("targetName"));
    }

    @Test(expected = ConfigurationException.class)
    public void testTargetAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.REFERENCE)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testTargetAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.REFERENCE)
                .withAttribute(ConfigAttributes.TARGET, "")
                .build();

        // when
        reader.read(element);
    }
}
