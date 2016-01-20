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
package org.hage.property.functions;


import org.hage.event.AbstractEvent;
import org.hage.monitor.IChangesNotifierMonitor;
import org.hage.property.*;
import org.hage.property.monitors.AbstractPropertyMonitor;
import org.hage.property.monitors.DefaultPropertyMonitorRule;
import org.hage.property.monitors.IPropertyMonitorRule;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;


@Ignore
public class FunctionsTestPropertyContainer implements IFunctionArgumentsResolver,
                                                       IPropertyContainer {

    private PropertiesSet _properties;
    private PropertyPathParser _pathParser;

    public FunctionsTestPropertyContainer() {
        _properties = new PropertiesSet();
        _pathParser = new PropertyPathParser(this);
    }

    public void addProperty(String name, Object value)
            throws DuplicatePropertyNameException {
        _properties.addProperty(new SimpleProperty(name, value));
    }

    public List<FunctionArgument> resolveArguments(String argumentsPattern) {
        ArrayList<FunctionArgument> result = new ArrayList<FunctionArgument>();

        for(Property property : _properties) {
            if(matchProperty(argumentsPattern, property)) {
                result.add(new FunctionArgument(this, property
                        .getMetaProperty().getName()));
            }
        }

        return result;
    }

    private boolean matchProperty(String argumentsNames, Property property) {
        return property.getMetaProperty().getName().startsWith(argumentsNames);
    }

    public MetaPropertiesSet getMetaPropertyContainer(
            boolean includeSubproperties) {
        return _properties.getMetaPropertiesSet();
    }

    public MetaProperty getMetaProperty(String propertyPath)
            throws InvalidPropertyOperationException,
                   InvalidPropertyPathException {
        return getProperty(propertyPath).getMetaProperty();
    }

    public Property getProperty(String propertyPath)
            throws InvalidPropertyPathException {
        return _pathParser.getPropertyForPath(propertyPath);
    }

    public void addFunction(PropertyFunction function)
            throws DuplicatePropertyNameException {
        _properties.addProperty(function);
    }

    public void addPropertyMonitor(String propertyPath, AbstractPropertyMonitor monitor)
            throws InvalidPropertyOperationException,
                   InvalidPropertyPathException {
        getProperty(propertyPath).addMonitor(monitor,
                                             new DefaultPropertyMonitorRule());
    }

    public void addPropertyMonitor(String propertyPath,
            AbstractPropertyMonitor monitor, IPropertyMonitorRule rule)
            throws InvalidPropertyOperationException,
                   InvalidPropertyPathException {
        getProperty(propertyPath).addMonitor(monitor, rule);

    }

    public void removePropertyMonitor(String propertyPath,
            AbstractPropertyMonitor monitor) throws InvalidPropertyOperationException,
                                                    InvalidPropertyPathException {
        getProperty(propertyPath).removeMonitor(monitor);
    }

    public void removeFunction(PropertyFunction function) {
        _properties.removeProperty(function);
    }

    public IPropertiesSet getProperties() {
        return _properties;
    }

    public MetaPropertiesSet getMetaProperties() {
        return _properties.getMetaPropertiesSet();
    }

    public void objectDeleted(AbstractEvent event) {
    }

    public MetaPropertiesSet getMetaPropertyContainer() {
        return _properties.getMetaPropertiesSet();
    }

    public void addMonitor(IChangesNotifierMonitor monitor) {

    }

    public void removeMonitor(IChangesNotifierMonitor monitor) {

    }


    private class SimpleProperty extends Property {

        private Object _value;
        private String _name;

        public SimpleProperty(String name, Object value) {
            _name = name;
            _value = value;
        }

        @Override
        public MetaProperty getMetaProperty() {
            Class<?> propertyClass = (_value == null ? Object.class : _value
                    .getClass());
            try {
                return new MetaProperty(_name, propertyClass, true, false);
            } catch(PropertyException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void setValue(Object value)
                throws InvalidPropertyOperationException {
            _value = value;
        }

        @Override
        public Object getValue() {
            return _value;
        }
    }

}
