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
 * File: PropertyTypeSynchronizer.java
 * Created: 08-09-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.persistence.service;

import java.util.List;

import org.hibernate.Session;
import org.jage.monitoring.persistence.config.HibernateConfiguration;
import org.jage.monitoring.persistence.dao.AbstractHibernateDao;

/**
 * Synchronizer of model data object.
 * 
 * @author AGH AgE Team
 */

public class DictionarySynchronizer extends AbstractHibernateDao{

	private Session hibernateSession;
	
	public DictionarySynchronizer(HibernateConfiguration hibernateConfiguration) {
		super(hibernateConfiguration);
	}
	
	/**
	 * Checks if exist the table which represents the given type of passed object.
	 * 
	 * @param type
	 * 			Type whose presence will be checked.
	 * @return
	 */
	public <T> T synchronizeDictionary(T type) {
		hibernateSession = hibernateConfiguration.openSession();
		List<T> list = hibernateSession.createQuery("from " + type.getClass().getSimpleName()).list();
		for (T typeElement : list) {
			if (typeElement.equals(type)) {
				hibernateSession.close();
				return typeElement;
			}
		}
		hibernateSession.close();
		return type;
	}
}
