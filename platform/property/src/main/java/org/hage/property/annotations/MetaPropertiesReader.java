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

package org.hage.property.annotations;


import org.hage.property.FieldMetaProperty;
import org.hage.property.GetterSetterMetaProperty;
import org.hage.property.PropertyException;
import org.hage.property.PropertyField;
import org.hage.property.PropertyGetter;
import org.hage.property.PropertySetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MetaPropertiesReader extends AbstractPropertiesReader {

    @Override
    protected String getGetterPropertyName(Method method) {
        PropertyGetter annotation = method.getAnnotation(PropertyGetter.class);
        return annotation != null ? annotation.propertyName() : null;
    }

    @Override
    protected String getSetterPropertyName(Method method) {
        PropertySetter annotation = method.getAnnotation(PropertySetter.class);
        return annotation != null ? annotation.propertyName() : null;
    }

    @Override
    protected GetterSetterMetaProperty createGetterSetterMetaProperty(String propertyName, Method getter, Method setter) throws PropertyException {
        PropertyGetter annotation = getter.getAnnotation(PropertyGetter.class);
        return new GetterSetterMetaProperty(propertyName, getter, setter, annotation.isMonitorable());
    }

    @Override
    protected GetterSetterMetaProperty createGetterMetaProperty(String propertyName, Method getter) throws PropertyException {
        PropertyGetter annotation = getter.getAnnotation(PropertyGetter.class);
        return new GetterSetterMetaProperty(propertyName, getter, annotation.isMonitorable());
    }

    @Override
    protected GetterSetterMetaProperty createSetterMetaProperty(String propertyName, Method setter) throws PropertyException {
        return new GetterSetterMetaProperty(propertyName, setter);
    }

    @Override
    protected String getFieldPropertyName(Field field) {
        PropertyField annotation = field.getAnnotation(PropertyField.class);
        return annotation != null ? annotation.propertyName() : null;
    }

    @Override
    protected FieldMetaProperty createFieldMetaProperty(Field field) throws PropertyException {
        PropertyField annotation = field.getAnnotation(PropertyField.class);
        return new FieldMetaProperty(annotation.propertyName(), field, annotation.isMonitorable());
    }
}
