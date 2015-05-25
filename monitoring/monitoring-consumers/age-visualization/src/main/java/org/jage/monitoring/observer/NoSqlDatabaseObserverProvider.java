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


package org.jage.monitoring.observer;

import org.jage.monitoring.config.ExecutorProvider;
import org.jage.platform.component.provider.IComponentInstanceProvider;

import com.typesafe.config.Config;

/**
 * Providers an instance of NoSqlDatabaseObserver class.
 * 
 * @author AGH AgE Team
 */
public class NoSqlDatabaseObserverProvider implements ObserverProvider {

	@Override
	public AbstractStatefulObserver create(final Config c, final IComponentInstanceProvider provider) {
		final String url = c.getString("url");
		final NoSqlDatabaseObserver nsdbc = new NoSqlDatabaseObserver(
				url, provider, provider.getInstance(ExecutorProvider.class)
		);
		return nsdbc;
	}

	@Override
	public String getType() {
		return "nosqldatabase";
	}
}