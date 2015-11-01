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
package org.jage.monitoring.visualization.storage.element;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


public class ComputationType implements TypeElement {

    String typeKey;
    private Map<String, ComputationInstance> instances = new LinkedHashMap<String, ComputationInstance>();

    public ComputationType(String typeKey) {
        this.typeKey = typeKey;
    }

    @Override
    public Collection<ComputationInstance> all() {
        return instances.values();
    }

    @Override
    public ComputationInstance get(String key) {
        return instances.get(key);
    }

    @Override
    public ComputationInstance last() {
        Collection<ComputationInstance> instancesCollection = instances.values();
        ComputationInstance last = new ComputationInstance("1");
        for(ComputationInstance computationInstance : instancesCollection) {
            if(new Long(computationInstance.getInstanceKey()) > new Long(last.getInstanceKey())) {
                last = computationInstance;
            }
        }
        return last;
    }

    public ComputationInstance put(ComputationInstance instance) {
        if(!instances.containsKey(instance.instanceKey)) {
            instances.put(instance.instanceKey, instance);
        }
        return instances.get(instance.instanceKey);
    }

    @Override
    public String toString() {
        return typeKey;
    }
}