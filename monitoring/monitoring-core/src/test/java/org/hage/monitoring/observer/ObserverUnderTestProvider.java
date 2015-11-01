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

package org.hage.monitoring.observer;


import com.typesafe.config.Config;
import org.hage.monitoring.config.ExecutorProvider;
import org.hage.platform.component.provider.IComponentInstanceProvider;


/**
 * Provides an instance of <code>ObserverUnderTest</code>.
 *
 * @author AGH AgE Team
 */
public class ObserverUnderTestProvider implements ObserverProvider {

    @Override
    public AbstractStatefulObserver create(final Config c, final IComponentInstanceProvider provider) {
        return new ObserverUnderTest(provider.getInstance(ExecutorProvider.class));
    }

    @Override
    public String getType() {
        return "observertest";
    }
}