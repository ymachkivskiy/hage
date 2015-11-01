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
package org.jage.monitoring.visualization.rest;


import org.jage.monitoring.visualization.storage.element.VisualDataStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Singleton which holds map of charts id and VisualDataStorage instances.
 *
 * @author AGH AgE Team
 */
public class StoragePointer {


    private static final Map<String, List<VisualDataStorage>> storageMap = new HashMap<String, List<VisualDataStorage>>();
    private static volatile StoragePointer instance = null;

    private StoragePointer() {
    }

    public static StoragePointer getInstance() {
        if(instance == null) {
            synchronized(StoragePointer.class) {
                if(instance == null) {
                    instance = new StoragePointer();
                }
            }
        }
        return instance;
    }


    /**
     * Gets VisualDataStorage instance by chart id.
     *
     * @param chartId
     * @return
     */
    public List<VisualDataStorage> pointStorages(String chartId) {
        return storageMap.get(chartId);
    }

    /**
     * Puts into storage map, passed values.
     *
     * @param chartId
     * @param storages
     */
    public void putStorages(String chartId, List<VisualDataStorage> storages) {
        storageMap.put(chartId, storages);
    }

}
