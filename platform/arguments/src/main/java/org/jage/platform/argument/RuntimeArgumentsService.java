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
 * Created: 2010-06-08
 * $Id$
 */

package org.jage.platform.argument;

import javax.annotation.CheckForNull;

/**
 * Runtime arguments service which delivers arguments to the platform itself and other services.
 * <p>
 * It introduces two types of options:
 * <ul>
 * <li>platform options - built-in options defined by node implementation,
 * <li>custom options - options defined by node services (in other modules).
 * </ul>
 * 
 * @author AGH AgE Team
 */
public interface RuntimeArgumentsService {

	/**
	 * Returns platform (built-in) option by a given key.
	 * 
	 * @param key
	 *            a key of a platform property.
	 * @return a required property or {@code null} if no option with the given key exists.
	 */
	@CheckForNull
	String getPlatformOption(String key);

	/**
	 * Checks if a platform option with a given key exists.
	 * 
	 * @param key
	 *            a key of a platform property.
	 * @return {@code true} if a platform property with the given key exists, {@code false} otherwise.
	 */
	boolean containsPlatformOption(String key);

	/**
	 * Returns custom option by a given key.
	 * 
	 * @param key
	 *            a key of a custom property.
	 * @return a required property or <code>null</code> if no option with the given key exists.
	 */
	@CheckForNull
	String getCustomOption(String key);

	/**
	 * Checks if a custom option with a given key exists.
	 * 
	 * @param key
	 *            a key of a custom property.
	 * @return a {@code true} if a custom property with the given key exists, {@code false} otherwise.
	 */
	boolean containsCustomOption(String key);

	/**
	 * Prints usage message which contains definition of platform options and way of specifying custom options.
	 */
	void printUsage();

}
