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
 * Created: 27-07-2012
 * $Id$
 */

package org.jage.platform.config.xml.util;

import org.dom4j.Document;

import static org.dom4j.DocumentHelper.createDocument;

import org.jage.platform.config.xml.ConfigTags;

/**
 * Utility builder for documents.
 *
 * @author AGH AgE Team
 */
public class DocumentBuilder {
	private final Document document = createDocument(ElementBuilder.element(ConfigTags.CONFIGURATION).build());

	/**
	 * Creates an empty configuration Document, with appropriate namespace settings.
	 *
	 * @return an empty document.
	 */
    public static DocumentBuilder emptyDocument() {
		return new DocumentBuilder();
	}

    /**
     * Adds an element, represented by an {@link ElementBuilder}, to the document being built.
     *
     * @param element the element to be added
     * @return this builder
     */
    public DocumentBuilder add(final ElementBuilder element) {
    	document.getRootElement().add(element.build());
    	return this;
    }

    /**
     * Returns the document being built.
     *
     * @return the document being built
     */
    public Document build() {
        return document;
    }
}