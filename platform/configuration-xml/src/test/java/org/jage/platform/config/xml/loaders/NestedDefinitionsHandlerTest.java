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

package org.jage.platform.config.xml.loaders;


import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigTags;
import org.jage.platform.config.xml.util.DocumentBuilder;
import org.junit.Test;

import static org.jage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.jage.platform.config.xml.util.ElementBuilder.*;


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
    public void shouldNotModifyValueAndReferenceElements() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(componentElement()
                             .withBody(
                                     constructorElement(valueElement(SOME_CLASS, SOME_NAME)),
                                     propertyElement(SOME_NAME, referenceElement(SOME_NAME))));
        final DocumentBuilder expected = emptyDocument()
                .add(componentElement()
                             .withBody(
                                     constructorElement(valueElement(SOME_CLASS, SOME_NAME)),
                                     propertyElement(SOME_NAME, referenceElement(SOME_NAME))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromConstructorArgAndProperty() throws ConfigurationException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";

        final DocumentBuilder original = emptyDocument()
                .add(componentElement()
                             .withBody(
                                     constructorElement(componentElement(innerName1)),
                                     propertyElement(SOME_NAME, componentElement(innerName2))));
        final DocumentBuilder expected = emptyDocument()
                .add(componentElement()
                             .withBody(
                                     constructorElement(referenceElement(innerName1)),
                                     componentElement(innerName1),
                                     propertyElement(SOME_NAME, referenceElement(innerName2)),
                                     componentElement(innerName2)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromListItem() throws ConfigurationException {
        shouldExtractElementFromCollectionItem(ConfigTags.LIST);
    }

    private void shouldExtractElementFromCollectionItem(final ConfigTags tag) throws ConfigurationException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";

        final DocumentBuilder original = emptyDocument()
                .add(element(tag)
                             .withBody(
                                     componentElement(innerName1),
                                     componentElement(innerName2)));
        final DocumentBuilder expected = emptyDocument()
                .add(element(tag)
                             .withBody(
                                     componentElement(innerName1),
                                     referenceElement(innerName1),
                                     componentElement(innerName2),
                                     referenceElement(innerName2)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractElementFromSetItem() throws ConfigurationException {
        shouldExtractElementFromCollectionItem(ConfigTags.SET);
    }

    @Test
    public void shouldExtractElementFromArrayItem() throws ConfigurationException {
        shouldExtractElementFromCollectionItem(ConfigTags.ARRAY);
    }

    @Test
    public void shouldExtractElementsFromMapItem() throws ConfigurationException {
        // given
        final String innerName1 = "inner1";
        final String innerName2 = "inner2";
        final String innerName3 = "inner3";
        final String innerName4 = "inner4";

        final DocumentBuilder original = emptyDocument()
                .add(mapElement()
                             .withBody(
                                     mapEntryElement(
                                             componentElement(innerName1),
                                             componentElement(innerName2)),
                                     mapEntryElement(
                                             componentElement(innerName3),
                                             componentElement(innerName4))));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement()
                             .withBody(
                                     mapEntryElement(
                                             referenceElement(innerName1),
                                             referenceElement(innerName2)),
                                     componentElement(innerName1),
                                     componentElement(innerName2),
                                     mapEntryElement(
                                             referenceElement(innerName3),
                                             referenceElement(innerName4)),
                                     componentElement(innerName3),
                                     componentElement(innerName4)));

        // then
        assertDocumentTransformation(original, expected);
    }
}
