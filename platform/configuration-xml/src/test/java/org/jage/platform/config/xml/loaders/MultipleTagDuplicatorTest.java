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

import org.junit.Test;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigTags;
import org.jage.platform.config.xml.util.DocumentBuilder;

import static org.jage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.jage.platform.config.xml.util.ElementBuilder.SOME_CLASS;
import static org.jage.platform.config.xml.util.ElementBuilder.SOME_NAME;
import static org.jage.platform.config.xml.util.ElementBuilder.componentElement;
import static org.jage.platform.config.xml.util.ElementBuilder.element;
import static org.jage.platform.config.xml.util.ElementBuilder.multipleElement;
import static org.jage.platform.config.xml.util.ElementBuilder.referenceElement;
import static org.jage.platform.config.xml.util.ElementBuilder.valueElement;

/**
 * Tests for MultipleTagDuplicator.
 *
 * @author AGH AgE Team
 */
public class MultipleTagDuplicatorTest extends AbstractDocumentLoaderTest<MultipleTagDuplicator> {

	@Override
	protected MultipleTagDuplicator getLoader() {
	    return new MultipleTagDuplicator();
	}

	@Test
	public void shouldDuplicateMultipleInList() throws ConfigurationException {
		shouldDuplicateMultipleInElement(ConfigTags.LIST);
	}

	@Test
	public void shouldDuplicateMultipleInArray() throws ConfigurationException {
		shouldDuplicateMultipleInElement(ConfigTags.ARRAY);
	}

	@Test
	public void shouldDuplicateMultipleInSet() throws ConfigurationException {
		shouldDuplicateMultipleComponentInElement(ConfigTags.SET);
	}

	@Test
	public void shouldDuplicateMultipleComponentInList() throws ConfigurationException {
		shouldDuplicateMultipleComponentInElement(ConfigTags.LIST);
	}

	@Test
	public void shouldDuplicateMultipleComponentInArray() throws ConfigurationException {
		shouldDuplicateMultipleComponentInElement(ConfigTags.ARRAY);
	}

	@Test
	public void shouldDuplicateMultipleComponentInSet() throws ConfigurationException {
		shouldDuplicateMultipleComponentInElement(ConfigTags.SET);
	}

	private void shouldDuplicateMultipleInElement(final ConfigTags tag) throws ConfigurationException {
		// given
		final DocumentBuilder original = emptyDocument()
			.add(element(tag)
				.withBody(
					multipleElement(3, referenceElement(SOME_NAME)),
					multipleElement(2, valueElement(SOME_CLASS, SOME_NAME))));
		final DocumentBuilder expected = emptyDocument()
			.add(element(tag)
				.withBody(
					referenceElement(SOME_NAME),
					referenceElement(SOME_NAME),
					referenceElement(SOME_NAME),
					valueElement(SOME_CLASS, SOME_NAME),
					valueElement(SOME_CLASS, SOME_NAME)));

		// then
		assertDocumentTransformation(original, expected);
	}

	private void shouldDuplicateMultipleComponentInElement(final ConfigTags tag) throws ConfigurationException {
		// given
		final String name1 = "name1";
		final String name2 = "name2";

		final DocumentBuilder original = emptyDocument()
			.add(element(tag)
				.withBody(
					multipleElement(3, componentElement(name1)),
	                multipleElement(2, componentElement(name2))));
		final DocumentBuilder expected = emptyDocument()
			.add(element(tag)
				.withBody(
					componentElement(name1),
					referenceElement(name1),
					referenceElement(name1),
					referenceElement(name1),
					componentElement(name2),
					referenceElement(name2),
					referenceElement(name2)));

		// then
		assertDocumentTransformation(original, expected);
	}
}
