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

package org.jage.monitoring.supplier;


import com.google.common.base.Supplier;
import com.typesafe.config.Config;
import org.jage.platform.component.provider.IComponentInstanceProvider;


/**
 * Class creates an instance of Supplier using reflection.
 *
 * @author AGH AgE Team
 */
public class ClassSupplierProvider implements SupplierProvider {

    @Override
    public Supplier<Object> create(final Config c, final IComponentInstanceProvider provider) {
        final String className = c.getString("arg");
        try {
            final Class<? extends Supplier> clazz = Class.forName(className).asSubclass(Supplier.class);
            return clazz.newInstance();
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getType() {
        return "class";
    }
}