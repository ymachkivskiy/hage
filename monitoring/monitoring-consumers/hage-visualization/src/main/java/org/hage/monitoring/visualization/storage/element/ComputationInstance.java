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
package org.hage.monitoring.visualization.storage.element;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author AGH AgE Team
 */
public class ComputationInstance implements DescriptionElement<VisualDataStorage> {

    String instanceKey;
    private Map<String, VisualDataStorage> storages = new LinkedHashMap<String, VisualDataStorage>();

    public ComputationInstance(String instanceKey) {
        this.instanceKey = instanceKey;
    }

    @Override
    public Collection<VisualDataStorage> all() {
        return storages.values();
    }

    @Override
    public VisualDataStorage get(String name) {
        return storages.get(name);
    }

    public void put(String id, VisualDataStorage storage) {
        storages.put(id, storage);
    }

    public String getInstanceKey() {
        return instanceKey;
    }

    public boolean containsVisualDataStorage(String key) {
        if(storages.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return instanceKey;
    }
}