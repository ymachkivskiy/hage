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


import org.jage.platform.component.definition.ConfigurationException;


/**
 * An interface representing reader which is responsible for creating {@link IComponentDescriptor} based on given
 * source.
 *
 * @author AGH AgE Team
 */
public interface IComponentDescriptorReader {

    /**
     * Analyzes a given source and than creates its component descriptor. Source type is dependent on concrete
     * implementation of the reader (for instance, source can be a Class object).
     *
     * @param source source object which will be analyzed to create a component descriptor
     * @return component descriptor
     * @throws ConfigurationException   when any error occurs during reading the source
     * @throws IllegalArgumentException when source has wrong type
     */
    IComponentDescriptor readDescritptor(Object source) throws ConfigurationException, IllegalArgumentException;

}
