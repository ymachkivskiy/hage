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

import org.dom4j.Document;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.util.io.ResourceLoader;

/**
 * Interface from loading a DOM document from some resource path. It is intended to be chained as consecutive decorators
 * applying changes to the resulting tree.
 *
 * @see ResourceLoader
 *
 * @author AGH AgE Team
 */
public interface DocumentLoader {

	/**
	 * Loads a Document from the given resource path.
	 *
	 * @param path the path to the resource
	 * @return a DOM Document
	 * @throws ConfigurationException if an error happens during the loading
	 */
	Document loadDocument(String path) throws ConfigurationException;
}
