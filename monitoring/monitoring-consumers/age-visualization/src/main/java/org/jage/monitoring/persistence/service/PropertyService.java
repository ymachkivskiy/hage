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
 * File: SavePropertyService.java
 * Created: 11-09-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.persistence.service;

import org.jage.monitoring.persistence.config.HibernateConfiguration;
import org.jage.monitoring.persistence.dao.PropertyDao;
import org.jage.monitoring.persistence.model.ComputationInstance;
import org.jage.monitoring.persistence.model.ComputationType;
import org.jage.monitoring.persistence.model.GathererName;

/**
 * Service responsible for persisting passed data.
 * 
 * @author AGH AgE Team
 */
public class PropertyService{

	private DictionarySynchronizer dictionarySynchronizer;
	
	private PropertyDao propertyDao;
	
	private ComputationType computationType;
	private ComputationInstance computationInstance;
	private GathererName gathererName;
	
	public PropertyService(HibernateConfiguration hibernateConfiguration){
		dictionarySynchronizer = new DictionarySynchronizer(hibernateConfiguration);
		propertyDao = new PropertyDao(hibernateConfiguration);
	}
	
	/**
	 * Persists passed data object.
	 *  
	 * @param name
	 * @param compInstanceName
	 * @param compTypeName
	 * @param timestamp
	 * @param data
	 */
	public void saveProperty(String name, String compInstanceName, String compTypeName, long timestamp, Object data) {
		gathererName = new GathererName(name);
		gathererName = dictionarySynchronizer.synchronizeDictionary(gathererName);
		
		computationType = new ComputationType(compTypeName);
		computationType = dictionarySynchronizer.synchronizeDictionary(computationType);
				
		computationInstance = new ComputationInstance(compInstanceName, computationType);
		computationInstance = dictionarySynchronizer.synchronizeDictionary(computationInstance);

		propertyDao.mergeProperty(gathererName, computationInstance, timestamp, data);

	}
}
