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

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;

import static org.dom4j.DocumentHelper.createXPath;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;
import org.jage.platform.config.xml.ConfigUtils;

import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toBoolean;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Maps.newHashMapWithExpectedSize;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.intersection;

/**
 * This decorator resolves configuration files referred to by {@code <include>} tags, processing {@code <block>} tags.
 * It requires some delegate to actually load the document.
 *
 * @author AGH AgE Team
 */
public final class DocumentResolver extends AbstractDocumentLoader {

	private static final XPath INCLUDES = initXpath(createXPath(format(
			"//%s:%s",
			ConfigNamespaces.DEFAULT.getPrefix(),
			ConfigTags.INCLUDE.toString())));
	private static final XPath ALL_BLOCKS = initXpath(createXPath(format(
			"//%s:%s",
			ConfigNamespaces.DEFAULT.getPrefix(),
			ConfigTags.BLOCK.toString())));
	private static final XPath CHILD_BLOCKS = initXpath(createXPath(format(
			"./%s:%s",
			ConfigNamespaces.DEFAULT.getPrefix(),
			ConfigTags.BLOCK.toString())));

	// This set keeps track of resolved documents, to detect circular dependencies
	private final Set<String> visitedDocuments = Sets.newLinkedHashSet();

	@Override
	public Document loadDocument(final String path) throws ConfigurationException {
		final Document document = getDelegate().loadDocument(path);
		try {
			if (!visitedDocuments.add(path)) {
				throw new ConfigurationException(format("Resources circular dependency: %s -> %s", visitedDocuments,
				        path));
			}
			resolveDocument(document);
		} catch (final ConfigurationException e) {
			final Throwable cause = e.getCause();
			throw new ConfigurationException(path + ": " + e.getMessage(), cause != null ? cause : e);
		} finally {
			visitedDocuments.remove(path);
		}
		return document;
	}

	@SuppressWarnings("unchecked")
	private void resolveDocument(final Document document) throws ConfigurationException {
		// Get ALL blocks defined in the document, including those from <include> elements.
		// Uniqueness of names is ensured, and will be an invariant of the following loop.
		final Map<String, Element> definedBlocks = asUniqueMap(selectNodes(ALL_BLOCKS, document));

		// Thorough the loop, we will make sure that the intersection of definedBlocks and includedBlocks is exactly
		// equal to overridingBlocks (possibly empty). As those blocks will be merged together, the later union of
		// definedBlocks and includedBlocks is bound to be name-unique as well, preserving the invariant.
		for (Element includeElement : selectNodes(INCLUDES, document)) {
			final Element parent = includeElement.getParent();
			final String includedDocumentName = getRequiredAttribute(includeElement, ConfigAttributes.FILE);

			// Get direct children blocks from the <include> element. Does not include nested ones.
			final Map<String, Element> overridingBlocks = asMap(selectNodes(CHILD_BLOCKS, includeElement));

			// Recurrently resolve the included document.
			final Document includedDocument = loadDocument(includedDocumentName);

			// Get all blocks defined in the included document.
			// Uniqueness is implied through recurrence.
			final Map<String, Element> includedBlocks = asMap(selectNodes(ALL_BLOCKS, includedDocument));

			// Check if overridingBlocks is a subset of includedBlocks.
			final Set<String> unknownBlocks = difference(overridingBlocks.keySet(), includedBlocks.keySet());
			if (!unknownBlocks.isEmpty()) {
				throw new ConfigurationException(format("Overriden blocks %s do not exist in included document %s",
				        unknownBlocks, includedDocumentName));
			}

			// Check if the intersection of definedBlocks and includedBlocks is exactly equal to commonBlocks.
			// It boils down to checking if no blocks defined elsewhere in this document already exist in the
			// included one.
			final Set<String> commonBlocks = intersection(definedBlocks.keySet(), includedBlocks.keySet());
			final Set<String> redefinedBlocks = difference(commonBlocks, overridingBlocks.keySet());
			if (!redefinedBlocks.isEmpty()) {
				throw new ConfigurationException(format("Document redefines blocks %s from the included document %s",
				        redefinedBlocks, includedDocumentName));
			}

			for (String name : overridingBlocks.keySet()) {
				final Element overridingBlock = overridingBlocks.get(name);
				final boolean shouldOverride = toBoolean(getRequiredAttribute(overridingBlock,
				        ConfigAttributes.OVERRIDE));
				final Element overridenBlock = includedBlocks.get(name);

				// If overriding should happen, clear the original block.
				if (shouldOverride) {
					overridenBlock.elements().clear();
				}
				// Otherwise, new content will be simply appended

				// Move all children elements from the overridingBlock to the overridenBlock
				addAll(overridenBlock.elements(), consumingIterable(overridingBlock.elements()));
			}

			// Now, as some previously existing blocks might have been removed from the included document because of
			// overrides, we need to recompute the map. It might now also include nested blocks from overriding ones.
			final Map<String, Element> overridenIncludedBlocks = asMap(selectNodes(ALL_BLOCKS, includedDocument));

			// overridingBlocks can be discarded, as only their content was moved.
			definedBlocks.keySet().removeAll(overridingBlocks.keySet());

			// As overridingBlocks have been removed, the intersection of definedBlocks and overridenIncludedBlocks is
			// now exactly equal to the set of unchanged nested blocks from overriding ones (potentially empty).
			// However, these already are in definedBlocks, and the following operation will not change their mapping.
			// As there are no other common blocks, we can be sure that the union of these two sets will preserve the
			// name-uniqueness invariant.
			definedBlocks.putAll(overridenIncludedBlocks);

			// We can now safely replace the <include> element with the included document content.

			// Get the <include> index in its parent and remove it.
			int index = parent.elements().indexOf(includeElement);
			parent.elements().remove(index);

			// Move all included elements to the parent, respecting relative and absolute order.
			for (Element child : consumingIterable((List<Element>)includedDocument.getRootElement().elements())) {
				parent.elements().add(index++, child);
			}
		}
	}

	private Map<String, Element> asUniqueMap(final List<Element> elements) throws ConfigurationException {
		final Map<String, Element> map = newHashMapWithExpectedSize(elements.size());
		for (Element element : elements) {
			final String name = ConfigUtils.getRequiredAttribute(element, ConfigAttributes.NAME);
			if (map.put(name, element) != null) {
				throw new ConfigurationException(format("Multiple definition of block %s", name));
			}
		}
		return map;
	}

	private Map<String, Element> asMap(final List<Element> elements) {
		return uniqueIndex(elements, new Function<Element, String>() {
			@Override
			public String apply(final Element input) {
				try {
					return getRequiredAttribute(input, ConfigAttributes.NAME);
				} catch (final ConfigurationException e) {
					throw Throwables.propagate(e);
				}
			}
		});
	}
}
