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
 * Pair that stores monitor and rule that decides whether the monitor should be informed about property change.
 *
 * @author AGH AgE Team
 *
 */
public class PropertyMonitorRulePair {

	private AbstractPropertyMonitor monitor;

	private IPropertyMonitorRule rule;

	/**
	 * Constructor.
	 *
	 * @param monitor
	 *            monitor.
	 * @param rule
	 *            rule.
	 */
	public PropertyMonitorRulePair(AbstractPropertyMonitor monitor, IPropertyMonitorRule rule) {
		this.monitor = monitor;
		this.rule = rule;
	}

	/**
	 * Returns monitor.
	 *
	 * @return monitor.
	 */
	public AbstractPropertyMonitor getPropertyMonitor() {
		return monitor;
	}

	/**
	 * Returns monitor's rule.
	 *
	 * @return monitor's rule.
	 */
	public IPropertyMonitorRule getPropertyMonitorRule() {
		return rule;
	}
}
