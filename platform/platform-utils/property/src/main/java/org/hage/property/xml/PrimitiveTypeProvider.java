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

package org.hage.property.xml;


import java.util.HashMap;
import java.util.Map;


/**
 * A provider of primitive types which maps string key of type to the property class.
 *
 * @author AGH AgE Team
 * @since 2.4.0
 */
public class PrimitiveTypeProvider {

    private static Map<String, Class<?>> builtInMap = new HashMap<String, Class<?>>();

    static {
        builtInMap.put("int", Integer.TYPE);
        builtInMap.put("long", Long.TYPE);
        builtInMap.put("double", Double.TYPE);
        builtInMap.put("float", Float.TYPE);
        builtInMap.put("bool", Boolean.TYPE);
        builtInMap.put("char", Character.TYPE);
        builtInMap.put("byte", Byte.TYPE);
        builtInMap.put("void", Void.TYPE);
        builtInMap.put("short", Short.TYPE);
    }

    /**
     * Gets a class of primitive property by a given name.
     *
     * @param name name of property type
     * @return property class or null if name is not correct
     */
    public static Class<?> getPrimitiveType(String name) {
        return builtInMap.get(name);
    }

}
