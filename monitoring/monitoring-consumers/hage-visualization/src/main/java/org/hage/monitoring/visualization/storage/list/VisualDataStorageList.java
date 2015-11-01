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
 * File: MonitoredDataPersisterImpl.java
 * Created: 29-12-2012
 * Author: kamilk
 * $Id$
 */

package org.hage.monitoring.visualization.storage.list;


import org.hage.monitoring.visualization.storage.StorageDescription;
import org.hage.monitoring.visualization.storage.VisualData;
import org.hage.monitoring.visualization.storage.element.VisualDataStorage;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * @author AGH AgE Team
 */

public class VisualDataStorageList implements VisualDataStorage {

    private LinkedList<VisualData> list;
    private Date createdDate;
    private String label;
    private StorageDescription storageDescription;

    public VisualDataStorageList(StorageDescription storageDescription) {
        list = new LinkedList<VisualData>();
        createdDate = new Date();
        this.label = storageDescription.toString();
        this.storageDescription = storageDescription;
    }

    @Override
    public void save(VisualData data) {
        list.add(data);
    }

    @Override
    public List<VisualData> getYoungerThan(Date date) {
        List<VisualData> result = new LinkedList<VisualData>();
        for(VisualData vd : list) {
            if(vd.getTimestamp() > date.getTime()) {
                result.add(vd);
            }
        }
        return result;
    }

    public StorageDescription getStorageDescription() {
        return storageDescription;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getLabel() {
        return label;
    }

    public void setSeriesDescription(StorageDescription storageDescription) {
        this.storageDescription = storageDescription;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public Collection<VisualData> all() {
        return list;
    }

    @Override
    public VisualData get(String key) {
        return null;
    }
}