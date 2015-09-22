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

package org.jage.platform.config.xml;

import java.util.List;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.loaders.AnonymousComponentsNamer;
import org.jage.platform.config.xml.loaders.ArgumentShortcutExtractor;
import org.jage.platform.config.xml.loaders.BlockUnwrapper;
import org.jage.platform.config.xml.loaders.ComponentsNormalizer;
import org.jage.platform.config.xml.loaders.DocumentLoader;
import org.jage.platform.config.xml.loaders.DocumentResolver;
import org.jage.platform.config.xml.loaders.MultipleTagDuplicator;
import org.jage.platform.config.xml.loaders.NestedDefinitionsHandler;
import org.jage.platform.config.xml.loaders.PlaceholderResolver;
import org.jage.platform.config.xml.loaders.RawDocumentLoader;
import org.jage.platform.config.xml.readers.DocumentReader;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of {@link IConfigurationLoader}. It uses a decorated chain of {@link DocumentLoader} to load a
 * resolved, normalized Document from some resource path, then a composed IDefinitionReader to read definitions from the
 * DOM tree.
 *
 * @author AGH AgE Team
 */
public final class XmlConfigurationLoader implements IConfigurationLoader {

	private static final Logger LOG = LoggerFactory.getLogger(XmlConfigurationLoader.class);

	private final DocumentReader reader;

	private final DocumentLoader loader;

	/**
	 * Creates a ConfigurationLoader.
	 *
	 * @throws ConfigurationException
	 *             if something goes wrong during construction.
	 */
	public XmlConfigurationLoader() throws ConfigurationException {
		this(createLoaderChain(), new DocumentReader());
	}

	private static DocumentLoader createLoaderChain() throws ConfigurationException {
    	return new RawDocumentLoader()
    		.append(new PlaceholderResolver())
    		.append(new DocumentResolver())
    		.append(new BlockUnwrapper())
    		.append(new AnonymousComponentsNamer())
    		.append(new ComponentsNormalizer())
    		.append(new NestedDefinitionsHandler())
    		.append(new ArgumentShortcutExtractor())
    		.append(new MultipleTagDuplicator());
    }

	XmlConfigurationLoader(final DocumentLoader loader, final DocumentReader reader) {
		this.loader = loader;
		this.reader = reader;
	}

	@Override
	public List<IComponentDefinition> loadConfiguration(final Object source) throws ConfigurationException {
		checkArgument(source instanceof String);
		final String path = (String)source;

		LOG.debug("Loading document from '{}'", path);
		final Document document = loader.loadDocument(path);

		final List<IComponentDefinition> definitions = reader.readDocument(document);
		LOG.debug("Read {} component definitions.", definitions.size());

		return definitions;
	}
}
