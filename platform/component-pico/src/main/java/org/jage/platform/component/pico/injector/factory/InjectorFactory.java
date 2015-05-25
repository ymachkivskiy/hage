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
 * Created: 2010-09-15
 * $Id$
 */

package org.jage.platform.component.pico.injector.factory;

import org.picocontainer.ComponentAdapter;

import org.jage.platform.component.definition.IComponentDefinition;

/**
 * This interface is used by factories that creates concrete instances of injectors.
 *
 * @author AGH AgE Team
 */
public interface InjectorFactory<D extends IComponentDefinition> {

	/**
	 * Creates an injector for the given instance provider and definition.
	 *
	 * @param <T>
	 *            the type of the injector
	 * @param definition
	 *            the definition
	 * @return an injector
	 */
	<T> ComponentAdapter<T> createAdapter(D definition);
}
