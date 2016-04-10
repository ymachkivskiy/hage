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

package org.hage.platform.simulationconfig.load.xml.util;


import org.dom4j.Element;
import org.dom4j.Namespace;
import org.hage.platform.simulationconfig.load.xml.ConfigAttributes;
import org.hage.platform.simulationconfig.load.xml.ConfigNamespaces;
import org.hage.platform.simulationconfig.load.xml.ConfigTags;

import static org.dom4j.DocumentHelper.*;


/**
 * Static utility methods for testing configuration.
 *
 * @author AGH AgE Team
 */
public final class ElementBuilder {

    public static final String EMPTY_STRING = "";
    public static final String ANY_TAG = "any";
    public static final String SOME_NAME = "someName";
    public static final String SOME_VALUE = "someValue";
    public static final String SOME_CLASS = "java.lang.String";
    public static final String IS_SINGLETON_VALUE = "true";
    private final Element element;

    public ElementBuilder(final ConfigTags tag) {
        this(tag.toString());
    }

    public ElementBuilder(final String elementName) {
        final Namespace namespace = createNamespace("", ConfigNamespaces.DEFAULT.getUri());
        element = createElement(createQName(elementName, namespace));
    }

    public static ElementBuilder anyElement() {
        return new ElementBuilder(ANY_TAG);
    }

    public static ElementBuilder componentElement() {
        return componentElement(SOME_NAME);
    }

    public static ElementBuilder componentElement(final String name) {
        return element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, name)
                .withAttribute(ConfigAttributes.CLASS, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

    public ElementBuilder withAttribute(final ConfigAttributes attribute, final String attributeValue) {
        element.addAttribute(attribute.toString(), attributeValue);
        return this;
    }

    public static ElementBuilder element(final ConfigTags tag) {
        return new ElementBuilder(tag);
    }

    public static ElementBuilder arrayElement() {
        return element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

    public static ElementBuilder listElement() {
        return element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

    public static ElementBuilder setElement() {
        return element(ConfigTags.SET)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

    public static ElementBuilder multipleElement(final int count, final ElementBuilder body) {
        return element(ConfigTags.MULTIPLE)
                .withAttribute(ConfigAttributes.COUNT, Integer.toString(count))
                .withBody(body);
    }

    public ElementBuilder withBody(final ElementBuilder... body) {
        for(final ElementBuilder innerElement : body) {
            element.add(innerElement.build());
        }
        return this;
    }

    public Element build() {
        return element;
    }

    public static ElementBuilder mapElement() {
        return element(ConfigTags.MAP)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.KEY_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE);
    }

    public static ElementBuilder mapEntryElement(final ElementBuilder key, final ElementBuilder value) {
        return element(ConfigTags.ENTRY).withBody(keyElement(key), value);
    }

    public static ElementBuilder keyElement(final ElementBuilder argument) {
        return element(ConfigTags.KEY).withBody(argument);
    }

    public static ElementBuilder valueElement(final String value) {
        return valueElement("String", value);
    }

    public static ElementBuilder valueElement(final String type, final String value) {
        return element(ConfigTags.VALUE)
                .withAttribute(ConfigAttributes.TYPE, type)
                .withContent(value);
    }

    public ElementBuilder withContent(final String value) {
        element.addText(value);
        return this;
    }

    public static ElementBuilder referenceElement(final String target) {
        return element(ConfigTags.REFERENCE)
                .withAttribute(ConfigAttributes.TARGET, target);
    }

    public static ElementBuilder constructorElement(final ElementBuilder body) {
        return constructorElement().withBody(body);
    }

    public static ElementBuilder constructorElement() {
        return element(ConfigTags.CONSTRUCTOR_ARG);
    }

    public static ElementBuilder propertyElement(final String propertyName, final ElementBuilder body) {
        return propertyElement(propertyName)
                .withBody(body);
    }

    public static ElementBuilder propertyElement(final String propertyName) {
        return element(ConfigTags.PROPERTY)
                .withAttribute(ConfigAttributes.NAME, propertyName);
    }

    public static ElementBuilder includeElement(final String file) {
        return element(ConfigTags.INCLUDE)
                .withAttribute(ConfigAttributes.FILE, file);
    }

    public static ElementBuilder blockElement(final String name) {
        return element(ConfigTags.BLOCK)
                .withAttribute(ConfigAttributes.NAME, name);
    }

    public static ElementBuilder blockElement(final String name, final boolean override) {
        return element(ConfigTags.BLOCK)
                .withAttribute(ConfigAttributes.NAME, name)
                .withAttribute(ConfigAttributes.OVERRIDE, Boolean.toString(override));
    }
}
