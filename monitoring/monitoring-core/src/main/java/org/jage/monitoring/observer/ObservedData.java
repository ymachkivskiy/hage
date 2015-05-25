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
package org.jage.monitoring.observer;

/**
 * Represents data processed by handler.
 * 
 * @author AGH AgE Team
 * 
 */
public class ObservedData {

	private final String name;
	private final long timestamp;
	private final Object data;

	public ObservedData(String name, long timestamp, Object data) {
		this.name = name;
		this.timestamp = timestamp;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Object getData() {
		return data;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null) {
			return false;
		}
		if(obj instanceof ObservedData){
			ObservedData od = (ObservedData)obj;
			return name.equals(od.name) && timestamp == od.timestamp && data == od.data;
		} else {
			return false;
		}
		
	}
}
