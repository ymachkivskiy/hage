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

package org.hage.platform.config.xml.loaders;


import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.XPath;
import org.hage.platform.config.xml.ConfigAttributes;
import org.hage.platform.config.xml.ConfigNamespaces;
import org.hage.platform.config.xml.ConfigTags;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonMap;
import static org.dom4j.DocumentHelper.createElement;
import static org.dom4j.DocumentHelper.createNamespace;
import static org.dom4j.DocumentHelper.createQName;


/**
 * Abstract implementation of {@link DocumentLoader}. Might hold some delegate, which subclasses may use.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractDocumentLoader implements DocumentLoader {

    private static final Namespace NAMESPACE = createNamespace("", ConfigNamespaces.DEFAULT.getUri());
    private DocumentLoader delegate;

    /**
     * Initializes the given xpath with the configuration namespace, then returns it.
     *
     * @param xpath a xpath to initialize
     * @return the initialized xpath
     */
    protected static final XPath initXpath(final XPath xpath) {
        final ConfigNamespaces configNamespace = ConfigNamespaces.DEFAULT;
        xpath.setNamespaceURIs(singletonMap(configNamespace.getPrefix(), configNamespace.getUri()));
        return xpath;
    }

    @SuppressWarnings("unchecked")
    protected static final List<Element> selectNodes(final XPath xpath, final Object node) {
        return xpath.selectNodes(node);
    }

    protected static final Element newReferenceElement(final String name) {
        final Element element = createElement(createQName(ConfigTags.REFERENCE.toString(), NAMESPACE));
        element.addAttribute(ConfigAttributes.TARGET.toString(), name);
        return element;
    }

    protected static final Element newValueElement(final String value) {
        return newValueElement("String", value);
    }

    protected static final Element newValueElement(final String type, final String value) {
        final Element element = createElement(createQName(ConfigTags.VALUE.toString(), NAMESPACE));
        element.addAttribute(ConfigAttributes.TYPE.toString(), type);
        element.addText(value);
        return element;
    }

    protected static final Element newKeyElement(final Element argument) {
        final Element element = createElement(createQName(ConfigTags.KEY.toString(), NAMESPACE));
        element.add(argument);
        return element;
    }

    /**
     * Return the delegate for this instance, if present.
     *
     * @return the delegate for this instance, or null if there is none
     */
    public DocumentLoader getDelegate() {
        return delegate;
    }

    /**
     * Utility building method for chaining Document Loaders. This instance will become the delegate for the provided
     * one. This method returns the given loader, so as to be chained.
     * <p>
     * Example usage:
     * <p>
     * <pre>
     * new A().append(new B()).append(new C());
     * </pre>
     * <p>
     * will return a reference to C and build a chain C -> B -> A, that is a call to C will be delegated to A, which
     * will produce some actual document, which will be first decorated by B, then by C and finally returned to the
     * caller.
     *
     * @param loader the next loader in the chain
     * @return the provided loader for chaining
     */
    public AbstractDocumentLoader append(final AbstractDocumentLoader loader) {
        loader.setDelegate(this);
        return loader;
    }

    /**
     * Set the delegate for this instance.
     *
     * @param delegate the delegate to set.
     */
    public void setDelegate(final DocumentLoader delegate) {
        this.delegate = checkNotNull(delegate);
    }
}
