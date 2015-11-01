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
 * Created: 26-07-2012
 * $Id$
 */

package org.hage.platform.config.xml.loaders;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.xml.ConfigTags;
import org.hage.platform.config.xml.util.DocumentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.config.xml.ConfigAttributes.*;
import static org.hage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.hage.platform.config.xml.util.ElementBuilder.*;


/**
 * Tests for ArgumentShortcutExtractor.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ArgumentShortcutExtractorTest extends AbstractDocumentLoaderTest<ArgumentShortcutExtractor> {

    @Override
    public ArgumentShortcutExtractor getLoader() {
        return new ArgumentShortcutExtractor();
    }

    @Test
    public void shouldExtractConstPropValueAttr() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement()
                             .withAttribute(TYPE, SOME_CLASS)
                             .withAttribute(VALUE, SOME_VALUE))
                .add(propertyElement(SOME_NAME)
                             .withAttribute(TYPE, SOME_CLASS)
                             .withAttribute(VALUE, SOME_VALUE));
        final DocumentBuilder expected = emptyDocument()
                .add(constructorElement(valueElement(SOME_CLASS, SOME_VALUE)))
                .add(propertyElement(SOME_NAME, valueElement(SOME_CLASS, SOME_VALUE)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractConstPropRefAttr() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement()
                             .withAttribute(REF, SOME_VALUE))
                .add(propertyElement(SOME_NAME)
                             .withAttribute(REF, SOME_VALUE));
        final DocumentBuilder expected = emptyDocument()
                .add(constructorElement(referenceElement(SOME_VALUE)))
                .add(propertyElement(SOME_NAME, referenceElement(SOME_VALUE)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropBothAttrs() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement()
                             .withAttribute(VALUE, SOME_VALUE)
                             .withAttribute(REF, SOME_VALUE));
        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropValueAttrAndContent() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement()
                             .withAttribute(TYPE, SOME_CLASS)
                             .withAttribute(VALUE, SOME_VALUE)
                             .withBody(anyElement()));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropRefAttrAndContent() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement()
                             .withAttribute(REF, SOME_VALUE)
                             .withBody(anyElement()));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropNoAttrsAndNoContent() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(constructorElement());

        // when
        tryDocumentTransformation(original);
    }

    @Test
    public void shouldExtractEntryKeyAttr() throws ConfigurationException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withBody(valueElement(value))));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement().withBody(
                        mapEntryElement(valueElement(key), valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryKeyRefAttr() throws ConfigurationException {
        // given
        final String keyRef = "key-ref";
        final String value = "value";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withBody(valueElement(value))));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement().withBody(
                        mapEntryElement(referenceElement(keyRef), valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryValueAttr() throws ConfigurationException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(VALUE, value)
                                .withBody(keyElement(valueElement(key)))));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement().withBody(
                        mapEntryElement(valueElement(key), valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryValueRefAttr() throws ConfigurationException {
        // given
        final String key = "key";
        final String valueRef = "value-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(VALUE_REF, valueRef)
                                .withBody(keyElement(valueElement(key)))));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement().withBody(
                        mapEntryElement(valueElement(key), referenceElement(valueRef))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryAllAttr() throws ConfigurationException {
        // given
        final String key = "key";
        final String keyRef = "key-ref";
        final String value = "value";
        final String valueRef = "value-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value),
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE_REF, valueRef),
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withAttribute(VALUE, value),
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withAttribute(VALUE_REF, valueRef)));
        final DocumentBuilder expected = emptyDocument()
                .add(mapElement().withBody(
                        mapEntryElement(valueElement(key), valueElement(value)),
                        mapEntryElement(valueElement(key), referenceElement(valueRef)),
                        mapEntryElement(referenceElement(keyRef), valueElement(value)),
                        mapEntryElement(referenceElement(keyRef), referenceElement(valueRef))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyBothAttrs() throws ConfigurationException {
        // given
        final String key = "key";
        final String keyRef = "key-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(KEY_REF, keyRef)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyAttrAndContent() throws ConfigurationException {
        // given
        final String key = "key";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withBody(keyElement(anyElement()))));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyRefAttrAndContent() throws ConfigurationException {
        // given
        final String keyRef = "key-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withBody(keyElement(anyElement()))));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyNoAttrsAndNoContent() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueBothAttrs() throws ConfigurationException {
        // given
        final String key = "key";
        final String value = "value";
        final String valueRef = "value-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value)
                                .withAttribute(VALUE_REF, valueRef)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueAttrAndContent() throws ConfigurationException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value)
                                .withBody(anyElement())));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueRefAttrAndContent() throws ConfigurationException {
        // given
        final String key = "key";
        final String valueRef = "value-ref";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE_REF, valueRef)
                                .withBody(anyElement())));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueNoAttrsAndNoContent() throws ConfigurationException {
        // given
        final String key = "key";
        final DocumentBuilder original = emptyDocument()
                .add(mapElement().withBody(
                        element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)));

        // when
        tryDocumentTransformation(original);
    }
}
