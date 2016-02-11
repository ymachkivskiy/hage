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
 * Created: 2012-08-06
 * $Id$
 */

package org.hage.platform.config.load.xml.loaders;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.DocumentBuilder;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import static com.google.common.base.Joiner.on;


/**
 * Tests for PlaceholderResolver.
 *
 * @author AGH AgE Team
 */
public class PlaceholderResolverTest extends AbstractDocumentLoaderTest<PlaceholderResolver> {

    private Properties systemProperties;

    @Override
    protected PlaceholderResolver getLoader() {
        return new PlaceholderResolver();
    }

    @Before
    public void saveProps() {
        systemProperties = System.getProperties();
        System.setProperties(new Properties(systemProperties));
    }

    @After
    public void loadProps() {
        System.setProperties(systemProperties);
    }

    @Test
    public void shouldResolvePlaceholdersFromSystemProperties() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String classPlaceholder = "age.class";
        final String classValue = "someClassValue";
        final String propertyName = "someProperty";
        final String propertyPlaceholder = "age.property";
        final String propertyValue = "somePropertyValue";
        final String isSingleton = "true";

        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, affix(classPlaceholder))
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(affix(propertyPlaceholder)))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, classValue)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(propertyValue))));

        // when
        System.setProperty(classPlaceholder, classValue);
        System.setProperty(propertyPlaceholder, propertyValue);

        // then
        assertDocumentTransformation(original, expected);
    }

    private String affix(final String value) {
        return PlaceholderResolver.PREFIX + value + PlaceholderResolver.SUFFIX;
    }

    @Test
    public void shouldResolvePlaceholdersFromSinglePropertiesFile() throws Exception {
        // given
        final String classPlaceholder = "age.class";
        final String classValue = "someClassValue";
        final String propertyName = "someProperty";
        final String propertyPlaceholder = "age.property";
        final String propertyValue = "somePropertyValue";
        final String isSingleton = "true";

        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, affix(classPlaceholder))
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(affix(propertyPlaceholder)))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, classValue)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(propertyValue))));

        // when
        final Properties properties = new Properties();
        properties.setProperty(classPlaceholder, classValue);
        properties.setProperty(propertyPlaceholder, propertyValue);

        final File file = File.createTempFile("age", null);
        properties.store(new FileWriter(file), "");
        System.setProperty(PlaceholderResolver.AGE_PROPERTIES_INCLUDE, file.toURI().toString());

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldResolvePlaceholdersFromMultiplePropertiesFiles() throws Exception {
        // given
        final String classPlaceholder = "age.class";
        final String classValue = "someClassValue";
        final String propertyName = "someProperty";
        final String propertyPlaceholder = "age.property";
        final String propertyValue = "somePropertyValue";
        final String isSingleton = "true";

        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, affix(classPlaceholder))
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(affix(propertyPlaceholder)))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, classValue)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, isSingleton)
                             .withBody(ElementBuilder.propertyElement(propertyName, ElementBuilder.valueElement(propertyValue))));

        // when
        final Properties properties1 = new Properties();
        properties1.setProperty(classPlaceholder, "someValueToBeOverriden");
        final File file1 = File.createTempFile("age", null);
        properties1.store(new FileWriter(file1), "");

        final Properties properties2 = new Properties();
        properties2.setProperty(classPlaceholder, classValue);
        properties2.setProperty(propertyPlaceholder, propertyValue);
        final File file2 = File.createTempFile("age", null);
        properties2.store(new FileWriter(file2), "");

        System.setProperty(PlaceholderResolver.AGE_PROPERTIES_INCLUDE, on(",").join(file1.toURI(), file2.toURI()));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfPlaceholderNotFound() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String classPlaceholder = "age.class";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.CLASS, affix(classPlaceholder)));

        // when
        tryDocumentTransformation(original);
    }
}
