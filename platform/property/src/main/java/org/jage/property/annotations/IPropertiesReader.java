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
package org.jage.property.annotations;

import java.util.Set;

import org.jage.property.FieldMetaProperty;
import org.jage.property.GetterSetterMetaProperty;
import org.jage.property.InvalidPropertyDefinitionException;
import org.jage.property.PropertyException;

public interface IPropertiesReader {

	public Set<FieldMetaProperty> readFieldMetaProperties(Class<?> clazz) throws InvalidPropertyDefinitionException, PropertyException;

	public Set<GetterSetterMetaProperty> readGetterSetterMetaProperties(Class<?> clazz) throws InvalidPropertyDefinitionException, PropertyException;
}
