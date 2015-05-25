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
 * Created: 2010-06-23
 * $Id$
 */

package org.jage.property.monitors;

/**
 * Default monitor rule, that always informs monitor about change.
 *
 * @author AGH AgE Team
 */
public class DefaultPropertyMonitorRule implements IPropertyMonitorRule {

	/**
	 * Checks whether the rule is active, and the monitor should be informed about the change.
	 *
	 * @param oldValue
	 *            old property's value.
	 * @param newValue
	 *            new property's value.
	 * @return true, if the monitor should be informed about the change; otherwise, returns false.
	 */
	public boolean isActive(Object oldValue, Object newValue) {
		return true;
	}
}
