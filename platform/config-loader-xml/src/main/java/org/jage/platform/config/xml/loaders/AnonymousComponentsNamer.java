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

package org.jage.platform.config.xml.loaders;

import java.util.UUID;

import static java.lang.String.format;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;

import static org.dom4j.DocumentHelper.createXPath;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import com.google.common.base.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This loader will decorate a document by providing random names to all anonymous definitions.
 *
 * @author AGH AgE Team
 */
public class AnonymousComponentsNamer extends AbstractDocumentLoader {

	// Heck, no XPath 2.0 available when this was written :(
	private static final XPath ANONYMOUS = initXpath(createXPath(format(
				"//%1$s:%3$s[not(@%2$s)] "
				+ "| //%1$s:%4$s[not(@%2$s)] "
				+ "| //%1$s:%5$s[not(@%2$s)]"
				+ "| //%1$s:%6$s[not(@%2$s)]"
				+ "| //%1$s:%7$s[not(@%2$s)]"
				+ "| //%1$s:%8$s[not(@%2$s)]"
				+ "| //%1$s:%9$s[not(@%2$s)]",
				ConfigNamespaces.DEFAULT.getPrefix(),
		        ConfigAttributes.NAME.toString(),
		        ConfigTags.COMPONENT.toString(),
				ConfigTags.AGENT.toString(),
				ConfigTags.STRATEGY.toString(),
				ConfigTags.ARRAY.toString(),
				ConfigTags.LIST.toString(),
				ConfigTags.SET.toString(),
				ConfigTags.MAP.toString())));

	private final Supplier<String> nameSupplier;

	/**
	 * Creates an {@link AnonymousComponentsNamer} with a random name supplier.
	 */
	public AnonymousComponentsNamer() {
		this(new RandomNameSupplier());
	}

	/**
	 * Creates an {@link AnonymousComponentsNamer} with the given name supplier.
	 * @param nameSupplier a name supplier for anonymous components
	 */
	public AnonymousComponentsNamer(final Supplier<String> nameSupplier) {
		this.nameSupplier = checkNotNull(nameSupplier);
	}

	@Override
	public Document loadDocument(final String path) throws ConfigurationException {
		final Document document = getDelegate().loadDocument(path);
		nameAnonymousElements(document);
		return document;
	}

	private void nameAnonymousElements(final Document document) {
		for (Element element : selectNodes(ANONYMOUS, document)) {
			final String name = nameSupplier.get();
			element.addAttribute(ConfigAttributes.NAME.toString(), name);
		}
	}

	private static final class RandomNameSupplier implements Supplier<String> {
		@Override
		public String get() {
			return UUID.randomUUID().toString();
		}
	}
}
