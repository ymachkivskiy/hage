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
 * Created: 2012-04-12
 * $Id$
 */

package org.hage.platform.config.load.xml.loaders;


import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.DocumentBuilder;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Test;


/**
 * Tests for MultipleTagDuplicator.
 *
 * @author AGH AgE Team
 */
public class MultipleTagDuplicatorTest extends AbstractDocumentLoaderTest<MultipleTagDuplicator> {

    @Override
    protected MultipleTagDuplicator getLoader() {
        return new MultipleTagDuplicator();
    }

    @Test
    public void shouldDuplicateMultipleInList() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleInElement(ConfigTags.LIST);
    }

    private void shouldDuplicateMultipleInElement(final ConfigTags tag) throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.multipleElement(3, ElementBuilder.referenceElement(ElementBuilder.SOME_NAME)),
                                     ElementBuilder.multipleElement(2, ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_NAME))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.referenceElement(ElementBuilder.SOME_NAME),
                                     ElementBuilder.referenceElement(ElementBuilder.SOME_NAME),
                                     ElementBuilder.referenceElement(ElementBuilder.SOME_NAME),
                                     ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_NAME),
                                     ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_NAME)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldDuplicateMultipleInArray() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleInElement(ConfigTags.ARRAY);
    }

    @Test
    public void shouldDuplicateMultipleInSet() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleComponentInElement(ConfigTags.SET);
    }

    private void shouldDuplicateMultipleComponentInElement(final ConfigTags tag) throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String name1 = "name1";
        final String name2 = "name2";

        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.multipleElement(3, ElementBuilder.componentElement(name1)),
                                     ElementBuilder.multipleElement(2, ElementBuilder.componentElement(name2))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.componentElement(name1),
                                     ElementBuilder.referenceElement(name1),
                                     ElementBuilder.referenceElement(name1),
                                     ElementBuilder.referenceElement(name1),
                                     ElementBuilder.componentElement(name2),
                                     ElementBuilder.referenceElement(name2),
                                     ElementBuilder.referenceElement(name2)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldDuplicateMultipleComponentInList() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleComponentInElement(ConfigTags.LIST);
    }

    @Test
    public void shouldDuplicateMultipleComponentInArray() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleComponentInElement(ConfigTags.ARRAY);
    }

    @Test
    public void shouldDuplicateMultipleComponentInSet() throws ConfigurationException, ConfigurationNotFoundException {
        shouldDuplicateMultipleComponentInElement(ConfigTags.SET);
    }
}
