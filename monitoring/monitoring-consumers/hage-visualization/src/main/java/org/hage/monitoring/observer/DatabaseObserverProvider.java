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
 * Providers an instance of DatabaseObserver class.
 *
 * @author AGH AgE Team
 */
public class DatabaseObserverProvider implements ObserverProvider {

    @Override
    public AbstractStatefulObserver create(final Config c, final IComponentInstanceProvider provider) {
        final String url = c.getString("url");
        final String user = c.getString("user");
        final String password = c.getString("password");
        final DatabaseObserver dc = new DatabaseObserver(
                url, user, password, provider, provider.getInstance(ExecutorProvider.class)
        );
        return dc;
    }

    @Override
    public String getType() {
        return "database";
    }
}
