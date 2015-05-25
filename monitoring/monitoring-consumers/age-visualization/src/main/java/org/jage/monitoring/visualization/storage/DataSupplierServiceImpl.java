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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jage.monitoring.visualization.rest.StoragePointer;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;

public class DataSupplierServiceImpl implements DataSupplierService {

	private static final long serialVersionUID = -7411329735802332922L;

	@Override
	public List<LinkedList<VisualData>> get(Map<Integer, Date> lastDateMap, String chartId) {
		List<LinkedList<VisualData>> result = new LinkedList<LinkedList<VisualData>>();
		StoragePointer storagePointer = StoragePointer.getInstance();
		List<VisualDataStorage> storageList = storagePointer.pointStorages(chartId);

		// pobierz magazyny i z kazdego z nich wyciagnij wszystki dane mlodsze niz data w mapie
		int i = 0;
		for (VisualDataStorage monitoredDataStorageList : storageList) {
//			result.add((LinkedList<VisualData>)monitoredDataStorageList.getTailFromN(n));
			LinkedList<VisualData> youngerThan = (LinkedList<VisualData>)monitoredDataStorageList.getYoungerThan(lastDateMap.get(i));
			if(!youngerThan.isEmpty()){
				result.add(youngerThan);
			}
			i++;
		}
		return result;
	}

}
