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
 * Created: 2010-03-08
 * $Id$
 */

package org.jage.platform.component.descriptor;

import java.util.List;

/**
 * An interface which describes a single component. It contains information about component type, its available
 * properties and more.
 * <p>
 * A component descriptor provides information about the real implementation of the component, whilst a component
 * definition describes its configuration.
 *
 * @author AGH AgE Team
 */
public interface IComponentDescriptor {

	/**
	 * Returns the component type.
	 *
	 * @return class of the described component
	 */
	Class<?> getComponentType();

//	/**
//	 * Returns all properties descriptions (meta-properties) of this component.
//	 *
//	 * @return list of meta properties
//	 */
//	Collection<MetaProperty> getProperties();

//	/**
//	 * Returns required properties descriptions (meta-properties) of this component. These properties are required to
//	 * proper component initialization.
//	 *
//	 * @return list of required meta properties
//	 */
//	Collection<MetaProperty> getRequriedProperties();

//	/**
//	 * Returns optional properties descriptions (meta-properties) of this component. These properties are not required
//	 * for proper component initialization.
//	 *
//	 * @return list of optional meta properties
//	 */
//	Collection<MetaProperty> getOptionalProperties();

	/**
	 * Returns list containing sequences of parameters types of available constructors.
	 *
	 * @return list of available constructor parameters
	 */
	List<List<Class<?>>> getConstructorParametersTypes();

	/**
	 * Checks if described component contains a property with a given name.
	 *
	 * @param name
	 *            name of a property
	 * @return true if this described component contains a needed property
	 */
	boolean containsProperty(String name);

}
