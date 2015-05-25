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
 * Created: 2010-09-13
 * $Id$
 */

package org.jage.platform.component.pico;

import java.lang.reflect.Type;
import java.util.List;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;

import org.jage.platform.component.provider.IMutableComponentInstanceProvider;

/**
 * Interface for implementations of {@link IMutableComponentInstanceProvider} based on Picocontainer library.
 *
 * @author AGH AgE Team
 */
public interface IPicoComponentInstanceProvider extends IMutableComponentInstanceProvider, MutablePicoContainer {

	/**
	 * Retrieves all component adapters inside this container that are associated with the specified parametrized type.
	 *
	 * @param type
	 *            the type of the components.
	 * @param typeParameters
	 *            an array of type parameters.
	 * @return a collection containing all the {@link ComponentAdapter}s inside this container that are associated with
	 *         the specified type. Changes to this collection will not be reflected in the container itself.
	 */
	List<ComponentAdapter<?>> getComponentAdapters(Class<?> type, Type[] typeParameters);

}
