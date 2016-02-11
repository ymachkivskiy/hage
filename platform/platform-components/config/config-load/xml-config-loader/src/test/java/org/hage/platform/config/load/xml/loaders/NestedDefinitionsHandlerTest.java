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
 * Created: 2012-04-10
 * $Id$
 */

package org.hage.platform.config.load.xml.loaders;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.DocumentBuilder;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Test;

import static org.hage.platform.config.load.xml.util.DocumentBuilder.emptyDocument;


/**
 * Tests for NestedDefinitionsHandler.
 *
 * @author AGH AgE Team
 */
public class NestedDefinitionsHandlerTest extends AbstractDocumentLoaderTest<NestedDefinitionsHandler> {

    @Override
    protected NestedDefinitionsHandler getLoader() {
        return new NestedDefinitionsHandler();
    }

    @Test
    public void shouldNotModifyValueAndReferenceElements() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(ElementBuilder.componentElement()
                             .withBody(
                                     ElementBuilder.constructorElement(ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_NAME)),
                                     ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.referenceElement(ElementBuilder.SOME_NAME))));
        final DocumentBuilder expected = emptyDocument()
                .add(ElementBuilder.componentElement()
                             .withBody(
                                     ElementBuilder.constructorElement(ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_NAME)),
                                     ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.referenceElement(ElementBuilder.SOME_NAME))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromConstructorArgAndProperty() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";

        final DocumentBuilder original = emptyDocument()
                .add(ElementBuilder.componentElement()
                             .withBody(
                                     ElementBuilder.constructorElement(ElementBuilder.componentElement(innerName1)),
                                     ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.componentElement(innerName2))));
        final DocumentBuilder expected = emptyDocument()
                .add(ElementBuilder.componentElement()
                             .withBody(
                                     ElementBuilder.constructorElement(ElementBuilder.referenceElement(innerName1)),
                                     ElementBuilder.componentElement(innerName1),
                                     ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.referenceElement(innerName2)),
                                     ElementBuilder.componentElement(innerName2)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromListItem() throws ConfigurationException, ConfigurationNotFoundException {
        shouldExtractElementFromCollectionItem(ConfigTags.LIST);
    }

    private void shouldExtractElementFromCollectionItem(final ConfigTags tag) throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";

        final DocumentBuilder original = emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.componentElement(innerName1),
                                     ElementBuilder.componentElement(innerName2)));
        final DocumentBuilder expected = emptyDocument()
                .add(ElementBuilder.element(tag)
                             .withBody(
                                     ElementBuilder.componentElement(innerName1),
                                     ElementBuilder.referenceElement(innerName1),
                                     ElementBuilder.componentElement(innerName2),
                                     ElementBuilder.referenceElement(innerName2)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromSetItem() throws ConfigurationException, ConfigurationNotFoundException {
        shouldExtractElementFromCollectionItem(ConfigTags.SET);
    }

    @Test
    public void shouldExtractElementFromArrayItem() throws ConfigurationException, ConfigurationNotFoundException {
        shouldExtractElementFromCollectionItem(ConfigTags.ARRAY);
    }

    @Test
    public void shouldExtractElementsFromMapItem() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";
        final String innerName3 = "inner3";
        final String innerName4 = "inner4";

        final DocumentBuilder original = emptyDocument()
                .add(ElementBuilder.mapElement()
                             .withBody(
                                     ElementBuilder.mapEntryElement(
                                             ElementBuilder.componentElement(innerName1),
                                             ElementBuilder.componentElement(innerName2)),
                                     ElementBuilder.mapEntryElement(
                                             ElementBuilder.componentElement(innerName3),
                                             ElementBuilder.componentElement(innerName4))));
        final DocumentBuilder expected = emptyDocument()
                .add(ElementBuilder.mapElement()
                             .withBody(
                                     ElementBuilder.mapEntryElement(
                                             ElementBuilder.referenceElement(innerName1),
                                             ElementBuilder.referenceElement(innerName2)),
                                     ElementBuilder.componentElement(innerName1),
                                     ElementBuilder.componentElement(innerName2),
                                     ElementBuilder.mapEntryElement(
                                             ElementBuilder.referenceElement(innerName3),
                                             ElementBuilder.referenceElement(innerName4)),
                                     ElementBuilder.componentElement(innerName3),
                                     ElementBuilder.componentElement(innerName4)));

        // then
        assertDocumentTransformation(original, expected);
    }
}
