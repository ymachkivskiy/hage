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
 * Enum for attributes names in AgE configuration files.
 *
 * @author AGH AgE Team
 */
public enum ConfigAttributes {
	NAME("name"),
	CLASS("class"),
	IS_SINGLETON("isSingleton"),
	TARGET("target"),
	VALUE("value"),
	TYPE("type"),
	KEY_TYPE("key-type"),
	VALUE_TYPE("value-type"),
	FILE("file"),
	COUNT("count"),
	OVERRIDE("override"),
	REF("ref"),
	KEY("key"),
	KEY_REF("key-ref"),
	VALUE_REF("value-ref");

	private final String value;

	private ConfigAttributes(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
