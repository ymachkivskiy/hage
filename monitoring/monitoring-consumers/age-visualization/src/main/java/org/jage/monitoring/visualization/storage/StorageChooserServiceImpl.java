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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.visualization.rest.StoragePointer;
import org.jage.monitoring.visualization.storage.element.ComputationInstance;
import org.jage.monitoring.visualization.storage.element.DescriptionElement;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;

import com.google.common.base.Splitter;

public class StorageChooserServiceImpl implements StorageChooserService {

    private static final long serialVersionUID = -217307941099348996L;

	public Collection<VisualDataStorage> getAndSaveVisualDataStorageCollectionByWildcard(StorageDescription storageDescription, String chartId, String numbersOfSelectedStorages){
		
		@SuppressWarnings("unchecked")
        List<ComputationInstance> computationInstanceCollection = (List<ComputationInstance>)VisualDataStorageFactory.getComputationInstanceCollection(storageDescription);
		List<VisualDataStorage> result = new LinkedList<VisualDataStorage>();
		if (numbersOfSelectedStorages != null) {
			Iterable<String> iterable = Splitter.on(',').split(numbersOfSelectedStorages);
			Iterator<String> iterator = iterable.iterator();
			List<ComputationInstance> selectedComputationInstanceCollection = new LinkedList<ComputationInstance>();
			while(iterator.hasNext()) {
				selectedComputationInstanceCollection.add(computationInstanceCollection.get(Integer.parseInt(iterator.next())-1));
            }
			for (ComputationInstance computationInstance : selectedComputationInstanceCollection) {
		        storageDescription.setComputationInstance(computationInstance.toString());
		        result.add((VisualDataStorage)VisualDataStorageFactory.getDescriptionElement(storageDescription));
	        }
		}
		StoragePointer storagePointer = StoragePointer.getInstance();
		storagePointer.putStorages(chartId, result);
		return result;
	}
	
	public VisualDataStorage getAndSaveVisualDataStorage(StorageDescription storageDescription, String chartId){
		VisualDataStorage visualDataStorage = (VisualDataStorage)VisualDataStorageFactory.getDescriptionElement(storageDescription);
		StoragePointer storagePointer = StoragePointer.getInstance();
		LinkedList<VisualDataStorage> list = new LinkedList<VisualDataStorage>(Arrays.asList(visualDataStorage));
		storagePointer.putStorages(chartId, list);
		return visualDataStorage;
	}
	
	public Collection<VisualDataStorage> getAndSaveVisualDataStorageCollection(StorageDescription storageDescription, String chartId, String numbersOfSelectedStorages) {
		DescriptionElement<VisualDataStorage> descriptionElement = VisualDataStorageFactory.getDescriptionElement(storageDescription);
		if(descriptionElement == null){
			throw new MonitoringException("No resource");
		}
		Collection<VisualDataStorage> computationDesc = descriptionElement.all();
		List<VisualDataStorage> storageList = new LinkedList<VisualDataStorage>(computationDesc);
		StoragePointer storagePointer = StoragePointer.getInstance();
		if (numbersOfSelectedStorages != null) {
			Iterable<String> iterable = Splitter.on(',').split(numbersOfSelectedStorages);
			Iterator<String> iterator = iterable.iterator();
			List<VisualDataStorage> selectedStorageList = new LinkedList<VisualDataStorage>();
			while(iterator.hasNext()) {
	            selectedStorageList.add(storageList.get(Integer.parseInt(iterator.next())-1));
            }
			storagePointer.putStorages(chartId, selectedStorageList);
			return selectedStorageList;
		}
		storagePointer.putStorages(chartId, storageList);
		return storageList;
	}
}