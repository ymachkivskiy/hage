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
import org.hage.event.ObjectChangedEvent;
import org.hage.event.PropertyEvent;
import org.hage.monitor.IChangesNotifierMonitor;
import org.hage.property.functions.DefaultFunctionArgumentsResolver;
import org.hage.property.functions.IFunctionArgumentsResolver;
import org.hage.property.functions.PropertyFunction;
import org.hage.property.monitors.AbstractPropertyMonitor;
import org.hage.property.monitors.DefaultPropertyMonitorRule;
import org.hage.property.monitors.IPropertyMonitorRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;


/**
 * Abstract implementation of {@link IPropertyContainer} which implements the basic operations.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractPropertyContainer implements IPropertyContainer {

    /**
     * Logger.
     */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Properties set held in this container.
     */
    protected PropertiesSet properties;

    /**
     * Property path parser.
     */
    protected PropertyPathParser pathParser;

    /**
     * Registered property monitors.
     */
    protected HashSet<IChangesNotifierMonitor> changesNotifierMonitors;

    /**
     * Arguments resolver.
     */
    protected IFunctionArgumentsResolver argumentsResolver;

    /**
     * Default constructor.
     */
    public AbstractPropertyContainer() {
        properties = new PropertiesSet();
        changesNotifierMonitors = new HashSet<IChangesNotifierMonitor>();
        pathParser = new PropertyPathParser(this);
    }

    // BEGIN Properties Accessors Methods

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#getProperty(java.lang.String)
     */
    public Property getProperty(String propertyPath) throws InvalidPropertyPathException {
        return pathParser.getPropertyForPath(propertyPath);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#addFunction(org.hage.property.functions.PropertyFunction)
     */
    public void addFunction(PropertyFunction function) throws DuplicatePropertyNameException {
        properties.addProperty(function);
        function.setArgumentsResolver(getFunctionArgumentsResolver());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#addPropertyMonitor(java.lang.String,
     * org.hage.property.monitors.AbstractPropertyMonitor)
     */
    public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException, InvalidPropertyPathException {
        addPropertyMonitor(propertyPath, monitor, new DefaultPropertyMonitorRule());
    }

    // END Properties Accessors Methods

    // BEGIN Property Monitors Methods

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#addPropertyMonitor(java.lang.String,
     * org.hage.property.monitors.AbstractPropertyMonitor, org.hage.property.monitors.IPropertyMonitorRule)
     */
    public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
            throws InvalidPropertyOperationException, InvalidPropertyPathException {
        Property property = pathParser.getPropertyForPath(propertyPath);
        property.addMonitor(monitor, rule);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#removePropertyMonitor(java.lang.String,
     * org.hage.property.monitors.AbstractPropertyMonitor)
     */
    public void removePropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException, InvalidPropertyPathException {
        Property property = pathParser.getPropertyForPath(propertyPath);
        property.removeMonitor(monitor);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#removeFunction(org.hage.property.functions.PropertyFunction)
     */
    public void removeFunction(PropertyFunction function) {
        if(properties.containsProperty(function.getMetaProperty().getName())) {
            properties.removeProperty(function);
            function.setArgumentsResolver(null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#getProperties()
     */
    public IPropertiesSet getProperties() {
        return properties;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#getMetaProperties()
     */
    public MetaPropertiesSet getMetaProperties() {
        return properties.getMetaPropertiesSet();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.property.IPropertyContainer#objectDeleted(org.hage.event.AbstractEvent)
     */
    public void objectDeleted(AbstractEvent event) {
        properties.objectDeleted(event);
    }

    /**
     * Factory method. Gets function arguments resolver for this container.
     *
     * @return function arguments resolver for this property container
     */
    protected IFunctionArgumentsResolver getFunctionArgumentsResolver() {
        if(argumentsResolver == null) {
            argumentsResolver = new DefaultFunctionArgumentsResolver(this);
        }
        return argumentsResolver;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hage.monitor.IChangesNotifier#addMonitor(org.hage.monitor.IChangesNotifierMonitor)
     */
    public void addMonitor(IChangesNotifierMonitor monitor) {
        changesNotifierMonitors.add(monitor);
    }

    // END Property Monitors Methods

    // BEGIN Property Function Methods

    /**
     * {@inheritDoc}
     *
     * @see org.hage.monitor.IChangesNotifier#removeMonitor(org.hage.monitor.IChangesNotifierMonitor)
     */
    public void removeMonitor(IChangesNotifierMonitor monitor) {
        changesNotifierMonitors.remove(monitor);
    }

    /**
     * Attaches new monitors to the all monitorable properties kept in this property container.
     */
    protected void attachMonitorsToProperties() {
        for(Property property : properties) {
            try {
                if(property.getMetaProperty().isMonitorable()) {
                    property.addMonitor(new PropertyMonitorForChangesNotifier(property),
                                        new DefaultPropertyMonitorRule());
                }
            } catch(InvalidPropertyOperationException ex) {
                log.error("Cannot attach monitor to monitorable property.", ex);
            }
        }
    }

    /**
     * Notifies all attached monitors ({@link IChangesNotifierMonitor}) that this object has been changed.
     */
    protected void notifyChangeNotiferMonitors() {
        for(IChangesNotifierMonitor monitor : changesNotifierMonitors) {
            monitor.objectChanged(this, new ObjectChangedEvent(this));
        }
    }

    // END Property Function Methods


    /**
     * This class is used to listen to changes in the PropertyContainer's inner properties.
     *
     * @author AGH AgE Team
     */
    private class PropertyMonitorForChangesNotifier extends AbstractPropertyMonitor {

        private Property property;

        public PropertyMonitorForChangesNotifier(Property property) {
            this.property = property;
        }

        public void propertyChanged(PropertyEvent event) {
            notifyChangeNotiferMonitors();
        }

        public void ownerDeleted(AbstractEvent event) {
            try {
                property.removeMonitor(this);
            } catch(InvalidPropertyOperationException ex) {
                log.error("Cannot unregister monitor from property.", ex);
            }
        }
    }

}
