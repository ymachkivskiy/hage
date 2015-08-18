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

package org.jage.platform.config.xml.loaders;

import static java.lang.String.format;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.XPath;

import static org.dom4j.DocumentHelper.createNamespace;
import static org.dom4j.DocumentHelper.createXPath;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigTags.COMPONENT;

/**
 * This decorator transforms {@code <agent>} and {@code <strategy>} tags to {@code <component>} ones, with appropriate
 * attributes. It requires some delegate to actually load the document.
 *
 * @author AGH AgE Team
 */
public final class ComponentsNormalizer extends AbstractDocumentLoader {

	private static final XPath AGENTS = initXpath(createXPath(format(
			"//%s:%s",
			ConfigNamespaces.DEFAULT.getPrefix(),
			ConfigTags.AGENT.toString())));

	private static final XPath STRATEGIES = initXpath(createXPath(format(
			"//%s:%s",
			ConfigNamespaces.DEFAULT.getPrefix(),
			ConfigTags.STRATEGY.toString())));


	@Override
	public Document loadDocument(final String path) throws ConfigurationException {
		final Document document = getDelegate().loadDocument(path);

		Namespace namespace = createNamespace("", ConfigNamespaces.DEFAULT.getUri());
		for (Element element : selectNodes(AGENTS, document)) {
			element.setQName(DocumentHelper.createQName(COMPONENT.toString(), namespace));
			element.addAttribute(IS_SINGLETON.toString(), "false");
		}
		for (Element element : selectNodes(STRATEGIES, document)) {
			element.setQName(DocumentHelper.createQName(COMPONENT.toString(), namespace));
			element.addAttribute(IS_SINGLETON.toString(), "true");
		}
		return document;
	}
}
