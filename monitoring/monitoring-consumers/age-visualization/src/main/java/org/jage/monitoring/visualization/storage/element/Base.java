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
 package org.jage.monitoring.visualization.storage.element;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  
 * 
 * @author AGH AgE Team
 *
 */
public class Base implements DescriptionElement<ComputationType>{
	private Map<String, ComputationType> types = new LinkedHashMap<String, ComputationType>();

	@Override
	public Collection<ComputationType> all() {
		return types.values();
	}

	@Override
	public ComputationType get(String key) {
		return types.get(key);
	}

	public ComputationType put(ComputationType type) {
		if (!types.containsKey(type.typeKey)) {
			types.put(type.typeKey, type);
		}
		return types.get(type.typeKey);
	}
}