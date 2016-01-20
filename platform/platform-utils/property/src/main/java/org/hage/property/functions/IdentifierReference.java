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


import org.hage.property.IPropertyContainer;
import org.hage.property.InvalidPropertyPathException;
import org.hage.property.PropertyException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Stores name of a single property identifier with list of indices.
 *
 * @author Tomek
 */
public class IdentifierReference {

    private String _identifierName;
    private List<Integer> _arrayIndices;

    /**
     * Constructor.
     *
     * @param identifierName
     * @param indices
     */
    public IdentifierReference(String identifierName, List<Integer> indices) {
        _identifierName = identifierName;
        _arrayIndices = indices;
    }

    /**
     * Returns all values for this property identifier definition. It this definition
     * is not array, returns value of the property; otherwise, returns array elements
     * for given indices.
     *
     * @param container
     * @return
     * @throws PropertyException
     */
    public List<Object> getAllValues(IPropertyContainer container) throws PropertyException {
        ArrayList<Object> currentValues = new ArrayList<Object>();

        Object initialValue = getInitialValue(container);

        if(initialValue != null) {
            currentValues.add(initialValue);

            for(Integer index : _arrayIndices) {
                ArrayList<Object> newValues = new ArrayList<Object>();
                for(Object currentValue : currentValues) {
                    if(index.intValue() != -1) {
                        newValues.add(Array.get(currentValue, index.intValue()));
                    } else {
                        int arraySize = Array.getLength(currentValue);
                        for(int i = 0; i < arraySize; i++) {
                            newValues.add(Array.get(currentValue, i));
                        }
                    }
                }
                currentValues = newValues;
            }
        }
        return currentValues;
    }

    private Object getInitialValue(IPropertyContainer container) throws PropertyException {
        if(_identifierName.startsWith("@")) {
            return getExtensionInitialValue(container);
        }
        try {
            if(container.getProperties().containsProperty(_identifierName)) {
                return container.getProperty(_identifierName).getValue();
            }
        } catch(InvalidPropertyPathException ex) {
        }
        return null;
    }

    protected Object getExtensionInitialValue(IPropertyContainer container) throws PropertyException {
        if(container != null) {
            Class<?> containerClass = container.getClass();
            Method methods[] = containerClass.getMethods();
            for(Method m : methods) {
                if((m.getName().equals("getAgents")) && (m.getParameterTypes().length == 0)) {
                    if(_identifierName.equals("@Agents")) {
                        List<?> agents = null;
                        try {
                            agents = (List<?>) m.invoke(container);
                        } catch(IllegalArgumentException e) {
                            throw new PropertyException(e);
                        } catch(IllegalAccessException e) {
                            throw new PropertyException(e);
                        } catch(InvocationTargetException e) {
                            throw new PropertyException(e);
                        }
                        return agents.toArray();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets name of the identifier.
     *
     * @return
     */
    public String getIdentifierName() {
        return _identifierName;
    }

    /**
     * Checks whether this identifier definition is array definition.
     *
     * @return
     */
    public boolean isArray() {
        return _arrayIndices.size() > 0;
    }
}
