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
 * Created: 2012-03-08
 * $Id$
 */

package org.jage.platform.config;

import java.util.Collection;

import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;

/**
 * An interface for components that want to be noticed about configuration events.
 * <p>
 * 
 * At the moment the only available event is "configuration update".
 * 
 * @author AGH AgE Team
 */
public interface ConfigurationChangeListener {

	/**
	 * Called when the computation configuration was updated.
	 * 
	 * @param componentDefinitions
	 *            new (updated) component definitions.
	 * @throws ComponentException
	 *             when the updated components are not a correct computation configuration.
	 */
	void computationConfigurationUpdated(Collection<IComponentDefinition> componentDefinitions)
	        throws ComponentException;
}
