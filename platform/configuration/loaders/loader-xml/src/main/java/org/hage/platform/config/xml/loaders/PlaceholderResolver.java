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

package org.hage.platform.config.xml.loaders;


import com.google.common.io.Closeables;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.xml.ConfigNamespaces;
import org.hage.util.io.IncorrectUriException;
import org.hage.util.io.Resource;
import org.hage.util.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import static com.google.common.base.Splitter.on;
import static java.lang.String.format;
import static org.dom4j.DocumentHelper.createXPath;


/**
 * This decorating document loader resolves placeholder values in configuration files.
 * <p>
 * Placeholders are values of attribute or text nodes, which begin by {@value #PREFIX} and end with {@value #SUFFIX}.
 * These values will be replaced by properties of the same name, looked up by this resolver.
 * <p>
 * The resolver will first look up properties from resources specified with the VM option
 * {@value #AGE_PROPERTIES_INCLUDE} (a possibly empty list of comma-separated resource paths). If there are no such, or
 * a given property is not found, the System properties will be looked up.
 *
 * @author AGH AgE Team
 */
public class PlaceholderResolver extends AbstractDocumentLoader {

    public static final String AGE_PROPERTIES_INCLUDE = "age.config.properties";

    public static final String INCLUDE_SEPARATOR = ",";
    public static final String PREFIX = "${";
    public static final String SUFFIX = "}";

    private static final XPath ATTRIBUTE_PLACEHOLDERS = initXpath(createXPath(format(
            "//%1$s:*/@*[starts-with(., '%2$s') and ends-with(., '%3$s')]" +
                    "| //%1$s:*[text()[starts-with(., '%2$s') and ends-with(., '%3$s')]]",
            ConfigNamespaces.DEFAULT.getPrefix(),
            PREFIX, SUFFIX)));

    @Override
    public Document loadDocument(final String path) throws ConfigurationException {
        final Document document = getDelegate().loadDocument(path);
        final Properties properties = loadProperties();
        fillPlaceholders(properties, document);
        return document;
    }

    private Properties loadProperties() throws ConfigurationException {
        final Properties properties = new Properties(System.getProperties());
        for(final String path : on(INCLUDE_SEPARATOR).trimResults().omitEmptyStrings()
                .split(System.getProperty(AGE_PROPERTIES_INCLUDE, ""))) {
            loadPropertyFile(properties, path);
        }
        return properties;
    }

    @SuppressWarnings("unchecked")
    private void fillPlaceholders(final Properties properties, final Document document) throws ConfigurationException {
        for(final Node node : (List<Node>) ATTRIBUTE_PLACEHOLDERS.selectNodes(document)) {
            final String text = node.getText();
            final String placeholderName = text.substring(PREFIX.length(), text.length() - SUFFIX.length());
            final String placeholderValue = properties.getProperty(placeholderName);
            if(placeholderValue == null) {
                throw new ConfigurationException(format("No property value found for placeholder '%s'", placeholderName));
            }
            node.setText(placeholderValue);
        }
    }

    private void loadPropertyFile(final Properties properties, final String path) throws ConfigurationException {
        InputStreamReader reader = null;
        try {
            final Resource resource = ResourceLoader.getResource(path);
            reader = new InputStreamReader(resource.getInputStream());
            properties.load(reader);
        } catch(final IncorrectUriException e) {
            throw new ConfigurationException(format("'%s' is not a valid properties path", path), e);
        } catch(final IOException e) {
            throw new ConfigurationException(format("Could not read properties from path '%s'", path), e);
        } finally {
            try {
                Closeables.close(reader, true);
            } catch(final IOException e) {
                assert false;
            }
        }
    }
}
