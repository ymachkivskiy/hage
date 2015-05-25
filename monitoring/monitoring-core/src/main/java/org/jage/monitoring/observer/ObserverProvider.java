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

import org.jage.platform.component.provider.IComponentInstanceProvider;

import com.typesafe.config.Config;

/**
 * The interface for providers of Observer instances.
 * 
 * @author AGH AgE Team
 */
public interface ObserverProvider {
	
	/**
	 * Creates <code>Observer</code> instance.  
	 * @param c 
	 * 			instance of Typesafe Config, which points on given observer definition in Typesafe configuration file. 
	 * @param provider
	 * 			used to inject dependencies if would be needed.
	 * @return observer.
	 */
	AbstractStatefulObserver create(Config c, IComponentInstanceProvider provider);
	
	/**
	 * @return String representation of data observer, used in Typesafe configuration file. 
	 */
	String getType();
}