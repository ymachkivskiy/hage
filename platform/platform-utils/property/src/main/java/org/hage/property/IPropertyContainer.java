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
 * Created: 2010-06-23
 * $Id$
 */

package org.hage.property;


import org.hage.event.AbstractEvent;
import org.hage.monitor.IChangesNotifier;
import org.hage.property.functions.PropertyFunction;
import org.hage.property.monitors.AbstractPropertyMonitor;
import org.hage.property.monitors.IPropertyMonitorRule;


/**
 * Interface which defines a property container with property monitors and property functions mechanisms.
 *
 * @author AGH AgE Team
 */
public interface IPropertyContainer extends IChangesNotifier {

    /**
     * Returns property with a given path.
     *
     * @param propertyPath path to the property.
     * @return Property with a given path.
     * @throws InvalidPropertyPathException invalid path to the property.
     */
    Property getProperty(String propertyPath) throws InvalidPropertyPathException;

    /**
     * Adds new property function to the container.
     *
     * @param function function to add.
     * @throws DuplicatePropertyNameException property or function with the same name already exists in this container.
     */
    void addFunction(PropertyFunction function) throws DuplicatePropertyNameException;

    /**
     * Adds monitor to property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to add.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invalid path to the property.
     */
    void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException, InvalidPropertyPathException;

    /**
     * Adds monitor with a rule to property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to add.
     * @param rule         monitor rule.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invlaid path to the property.
     */
    void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
            throws InvalidPropertyOperationException, InvalidPropertyPathException;

    /**
     * Unregisters monitor from property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to unergister.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invalid path to the property.
     */
    void removePropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException, InvalidPropertyPathException;

    /**
     * Removes property function. If the function doesn't belong to this container, this method doesn't throw any
     * exception.
     *
     * @param function function to remove.
     */
    void removeFunction(PropertyFunction function);

    /**
     * Returns set that stores all properties (not including subproperties).
     *
     * @return set that stores all properties.
     */
    IPropertiesSet getProperties();

    /**
     * Returns set that stores all meta properties (not including subproperties).
     *
     * @return set that stores all subproperties.
     */
    MetaPropertiesSet getMetaProperties();

    /**
     * Informs the container that it has been deleted.
     *
     * @param event the event
     */
    void objectDeleted(AbstractEvent event);

}
