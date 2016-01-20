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
package org.hage.property.testHelpers;


import org.hage.property.ClassPropertyContainer;
import org.hage.property.PropertyField;
import org.hage.property.PropertyGetter;
import org.hage.property.PropertySetter;


public class InnerClassWithProperties extends ClassPropertyContainer {

    @SuppressWarnings("unused")
    @PropertyField(propertyName = "intProperty")
    private int _intProperty = 2;

    private String _stringProperty = "stringProperty";

    public InnerClassWithProperties() {
    }

    @PropertyGetter(propertyName = "stringProperty")
    public String getStringProperty() {
        return _stringProperty;
    }

    @PropertySetter(propertyName = "stringProperty")
    public void setStringProperty(String value) {
        _stringProperty = value;
        notifyMonitorsForChangedProperty("stringProperty");
    }
}
