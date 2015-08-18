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

/**
 * Enum for namespaces in AgE configuration files.
 *
 * @author AGH AgE Team
 */
public enum ConfigNamespaces {
	DEFAULT("age", "http://age.iisg.agh.edu.pl/schema/age");

	private final String prefix;

	private final String uri;

	private ConfigNamespaces(final String prefix, final String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}

	/**
	 * Get the prefix of this namespace.
	 *
	 * @return the prefix of this namespace
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Get the URI of this namespace.
	 *
	 * @return the URI of this namespace
	 */
	public String getUri() {
		return uri;
	}
}
