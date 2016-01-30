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
 * Created: 2009-01-08
 * $Id$
 */

package org.hage.platform.component.action;


import org.hage.platform.component.provider.IComponentInstanceProvider;

import java.lang.reflect.Type;
import java.util.Collection;


/**
 * A instance provider that always returns null
 *
 * @author AGH AgE Team
 */
public class NullInstanceProvider implements IComponentInstanceProvider {

    /*
     * (non-Javadoc)
     *
     * @see org.hage.common.config.IComponentInstanceProvider#getInstance(java.lang.String)
     */
    @Override
    public Object getInstance(String name) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.hage.common.config.IComponentInstanceProvider#getInstance(java.lang.Class)
     */
    @Override
    public <T> T getInstance(Class<T> type) {
        return null;
    }

    @Override
    public <T> T getParametrizedInstance(Class<T> type, Type[] typeParameters) {
        return null;
    }

    @Override
    public <T> Collection<T> getInstances(Class<T> type) {
        return null;
    }

    @Override
    public Class<?> getComponentType(String name) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.hage.common.config.IComponentInstanceProvider#getInstance()
     */
    public Object getInstance() {
        return null;
    }

    public String getName() {
        return "";
    }

}
