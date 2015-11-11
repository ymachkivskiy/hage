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


import org.hage.property.InvalidPropertyOperationException;
import org.hage.property.MetaProperty;
import org.hage.property.Property;
import org.hage.property.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Base class for all property functions.
 *
 * @author Tomek
 */
public abstract class PropertyFunction extends Property {

    private static final Logger _logger = LoggerFactory.getLogger(PropertyFunction.class);

    private IFunctionArgumentsResolver _argumentsResolver;
    private String _argumentsPattern;
    private String _functionName;

    /**
     * Constructor.
     *
     * @param functionName     name of the function.
     * @param argumentPatterns pattern for arguments.
     */
    protected PropertyFunction(String functionName, String argumentPatterns) {
        _functionName = functionName;
        _argumentsPattern = argumentPatterns;
        initializeMonitoringStrategy();
    }

    /**
     * Returns metadata for the property function.
     */
    @Override
    public MetaProperty getMetaProperty() {
        try {
            return new MetaProperty(_functionName, getReturnType(), false, true);
        } catch(PropertyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Because functions are read-only, this operation always throws
     * InvalidPropertyOperationException.
     */
    @Override
    public void setValue(Object value) throws InvalidPropertyOperationException {
        throw new InvalidPropertyOperationException("Cannot set value to property function.");
    }

    /**
     * Gets value of the function.
     * Because this method overrides Property.getValue() method that doesn't throw
     * any exception, when error occurs, it is caught, error message is logged and
     * this function returns null.
     */
    @Override
    public Object getValue() {
        if(_argumentsResolver == null) {
            return null;
        }
        try {
            return computeValue(_argumentsResolver.resolveArguments(_argumentsPattern));
        } catch(InvalidArgumentsPatternException ex) {
            _logger.error("Invalid arguments pattern.", ex);
            return null;
        } catch(InvalidFunctionArgumentException ex) {
            _logger.error("Invalid function argument.", ex);
            return null;
        }
    }

    /**
     * Computes value of the function.
     *
     * @param arguments arguments for the function.
     * @return value of the function.
     * @throws InvalidFunctionArgumentException one of arguments is not valid.
     */
    protected abstract Object computeValue(List<FunctionArgument> arguments)
            throws InvalidFunctionArgumentException;

    /**
     * Gets class for function's return value, which is used to construct metadata.
     *
     * @return class for function's return value.
     */
    protected abstract Class<?> getReturnType();

    /**
     * Sets arguments resolver for this function.
     *
     * @param resolver
     */
    public void setArgumentsResolver(IFunctionArgumentsResolver resolver) {
        _argumentsResolver = resolver;
    }
}
