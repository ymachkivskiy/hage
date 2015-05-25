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
package org.jage.monitoring.visualization.storage;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.visualization.storage.element.Base;
import org.jage.monitoring.visualization.storage.element.ComputationInstance;
import org.jage.monitoring.visualization.storage.element.ComputationType;
import org.jage.monitoring.visualization.storage.element.DescriptionElement;
import org.jage.monitoring.visualization.storage.element.TypeElement;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;
import org.jage.monitoring.visualization.storage.mongodb.StorageLoaderMongo;
import org.jage.monitoring.visualization.storage.mongodb.VisualDataStorageMongo;

public class VisualDataStorageFactory {

	private static final Base base = new Base();
		
	/**
	 * Creates storages saved in database.
	 */
	public static void loadSavedStorages(){
		StorageLoader storageLoader = new StorageLoaderMongo();
		Collection<StorageDescription> storageDescriptions = storageLoader.loadSavedStorageDescriptions();
		for (StorageDescription storageDescription : storageDescriptions) {
	        createStorage(new StorageDescription(
	        		storageDescription.getComputationType(), 
	        		storageDescription.getComputationInstance(), 
	        		storageDescription.getGathererId()
	        	)
	        );
        }
	}
	
	/**
	 * Creates new VisualDataStorage.
	 * 
	 * @param storageDescription
	 * @return
	 * 			Created VisualDataStorage instance.
	 */
	public static VisualDataStorage createStorage(StorageDescription storageDescription){
		VisualDataStorage mds = new VisualDataStorageMongo(storageDescription);
		if (storageDescription.getComputationType() != null 
				&& storageDescription.getComputationInstance() != null
		        && storageDescription.getGathererId() != null) {
			base.put(new ComputationType(storageDescription.getComputationType()))
				.put(new ComputationInstance(storageDescription.getComputationInstance()))
				.put(storageDescription.getGathererId(), mds);
		}
		return mds;
	}
	
	public static DescriptionElement getDescriptionElement(StorageDescription storageDescription){
		
		List<String> descriptionParts = changeStorageDescriptionToList(storageDescription);
		
		return findDescriptionElement(base, descriptionParts);
	}
	
	public static Collection<ComputationInstance> getComputationInstanceCollection(StorageDescription storageDescription){
		
		List<String> queries = changeStorageDescriptionToList(storageDescription);
		
		Collection<ComputationInstance> result = new LinkedList<ComputationInstance>();
		ComputationType computationType = (ComputationType)base.get(queries.get(0));
		if(computationType==null){
			throw new MonitoringException("No resource");
		}
		Collection<ComputationInstance> computationInstanceCollection = computationType.all();
		for (ComputationInstance computationInstance : computationInstanceCollection) {
	        if(computationInstance.containsVisualDataStorage(queries.get(2))){
	        	result.add(computationInstance);
	        }
        }
		return result;
	}

	public static VisualDataStorage getOrCreateVisualDataStorage(StorageDescription storageDescription){
	
		List<String> descriptionParts = changeStorageDescriptionToList(storageDescription);
		VisualDataStorage computationDesc = (VisualDataStorage) findDescriptionElement(base, descriptionParts);
		
		if(computationDesc!=null){
			return computationDesc;
		}
		
		return createStorage(storageDescription);
	}
	
	public static Collection<? extends DescriptionElement> getComputationElementCollection(StorageDescription storageDescription) {
		VisualDataStorageFactory.loadSavedStorages();
		DescriptionElement descriptionElement = VisualDataStorageFactory.getDescriptionElement(storageDescription);
		if(descriptionElement==null)
			throw new MonitoringException("No resource");
		Collection<? extends DescriptionElement> computationDesc = descriptionElement.all(); 
		return new LinkedList<DescriptionElement>(computationDesc);
	}
	
    private static DescriptionElement<?> findDescriptionElement(DescriptionElement<?> de, List<String> queries){
		
		DescriptionElement<?> descElem = de;
		
		int i = 0;
		for(String q : queries){
			if(i==1 && q.equals("last")){
				descElem = ((TypeElement)descElem).last();
			} else {
				descElem = (DescriptionElement<?>)descElem.get(q);
			}
			i++;
			if(descElem == null){
				return null;
			}
		}
		return descElem;
	}

	private static List<String> changeStorageDescriptionToList(StorageDescription storageDescription){
		List<String> descriptionParts = new LinkedList<String>();
		
		if(storageDescription.getComputationType() != null)
			descriptionParts.add(storageDescription.getComputationType());
		if(storageDescription.getComputationInstance() != null)
			descriptionParts.add(storageDescription.getComputationInstance());
		if(storageDescription.getGathererId() != null)
			descriptionParts.add(storageDescription.getGathererId());
		
		return descriptionParts;
	}
}
