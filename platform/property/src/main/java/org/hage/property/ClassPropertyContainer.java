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
package org.hage.property;


import org.hage.event.AbstractEvent;
import org.hage.monitor.IChangesNotifierMonitor;
import org.hage.property.functions.PropertyFunction;
import org.hage.property.monitors.AbstractPropertyMonitor;
import org.hage.property.monitors.DefaultPropertyMonitorRule;
import org.hage.property.monitors.IPropertyMonitorRule;

import java.util.ArrayList;


/**
 * IPropertyContainer implementation that reads properties from annotated fields
 * and methods.
 *
 * @author Tomek, awos
 */
public class ClassPropertyContainer extends AbstractPropertyContainer {

    private boolean _propertiesLoaded;

    /**
     * Constructor.
     */
    public ClassPropertyContainer() {
        super();
        _propertiesLoaded = false;
    }

    private synchronized void loadProperties() {
        if(_propertiesLoaded) {
            return;
        }
        try {
            properties = getPropertiesFactory().getAllProperties(this);
        } catch(InvalidPropertyDefinitionException ex) {
            log.error(ex.toString());
            properties = new PropertiesSet();
        }
        _propertiesLoaded = true;
        for(Property property : properties) {
            Object propertyValue = property.getValue();
            property.notifyMonitors(propertyValue);
        }

        attachMonitorsToProperties();

    }

    /**
     * Returns property with a given path.
     *
     * @param propertyPath path to the property.
     * @return Property with a given path.
     * @throws InvalidPropertyPathException invalid path to the property.
     */
    @Override
    public Property getProperty(String propertyPath)
            throws InvalidPropertyPathException {
        loadProperties();
        return super.getProperty(propertyPath);
    }

    /**
     * Adds new property function to the container.
     *
     * @param function function to add.
     * @throws DuplicatePropertyNameException property or function with the same name already exists in
     *                                        this container.
     */
    @Override
    public void addFunction(PropertyFunction function)
            throws DuplicatePropertyNameException {
        loadProperties();
        super.addFunction(function);
    }

    /**
     * Adds monitor to property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to add.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invalid path to the property.
     */
    @Override
    public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException,
                   InvalidPropertyPathException {
        addPropertyMonitor(propertyPath, monitor,
                           new DefaultPropertyMonitorRule());
    }

    /**
     * Adds monitor with a rule to property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to add.
     * @param rule         monitor rule.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invlaid path to the property.
     */
    @Override
    public void addPropertyMonitor(String propertyPath,
            AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
            throws InvalidPropertyOperationException,
                   InvalidPropertyPathException {
        loadProperties();
        super.addPropertyMonitor(propertyPath, monitor, rule);
    }

    /**
     * Unregisters monitor from property with a given path.
     *
     * @param propertyPath path to the property.
     * @param monitor      monitor to unergister.
     * @throws InvalidPropertyOperationException property with the given path is not monitorable.
     * @throws InvalidPropertyPathException      invalid path to the property.
     */
    @Override
    public void removePropertyMonitor(String propertyPath,
            AbstractPropertyMonitor monitor) throws InvalidPropertyOperationException,
                                                    InvalidPropertyPathException {
        loadProperties();
        super.removePropertyMonitor(propertyPath, monitor);
    }

    /**
     * Removes property function. If the function doesn't belong to this
     * container, this method doesn't throw any exception.
     *
     * @param function function to remove.
     */
    @Override
    public void removeFunction(PropertyFunction function) {
        loadProperties();
        super.removeFunction(function);
    }

    /**
     * Returns set that stores all properties (not including subproperties).
     *
     * @return set that stores all properties.
     */
    @Override
    public IPropertiesSet getProperties() {
        loadProperties();
        return super.getProperties();
    }

    @Override
    public synchronized MetaPropertiesSet getMetaProperties() {
        if(!_propertiesLoaded) {
            try {
                return getPropertiesFactory().getAllMetaProperties(this);
            } catch(InvalidPropertyDefinitionException ex) {
                log.error(ex.toString());
                return null;
            }
        } else {
            return super.getMetaProperties();
        }
    }

    /**
     * Informs the container that it has been deleted.
     *
     * @param event
     */
    @Override
    public void objectDeleted(AbstractEvent event) {
        loadProperties();
        super.objectDeleted(event);
    }

    @Override
    protected void attachMonitorsToProperties() {
        loadProperties();
        super.attachMonitorsToProperties();
    }

    /**
     * Factory method. Returns properties factory that should be used to
     * construct properties set.
     *
     * @return
     */
    protected IClassPropertiesFactory getPropertiesFactory() {
        return ClassAnnotatedPropertiesFactory.INSTANCE;
    }

    protected void notifyMonitorsForChangedProperties() {
        for(Property property : properties) {
            property.notifyMonitors(property.getValue(), false);
        }
    }

    /**
     * Notifies monitor about property change.
     *
     * @param propertyPath path to the property that has been changed.
     */
    protected void notifyMonitorsForChangedProperty(String propertyPath) {
        loadProperties();

        Property property = properties.getProperty(propertyPath);
        if(property == null) {
            return;
        }

        if(!property.getMetaProperty().isMonitorable()) {
            return;
        }

        property.notifyMonitors(property.getValue());
    }

    protected void notifyMonitorsAboutDeletion(AbstractEvent event) {
        ArrayList<IChangesNotifierMonitor> monitorsCopy = new ArrayList<IChangesNotifierMonitor>(
                changesNotifierMonitors);
        for(IChangesNotifierMonitor monitor : monitorsCopy) {
            monitor.ownerDeleted(event);
        }
    }

}
