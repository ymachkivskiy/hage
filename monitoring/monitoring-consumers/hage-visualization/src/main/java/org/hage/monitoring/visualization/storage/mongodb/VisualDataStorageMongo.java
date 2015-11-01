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
package org.hage.monitoring.visualization.storage.mongodb;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.hage.monitoring.visualization.storage.StorageDescription;
import org.hage.monitoring.visualization.storage.VisualData;
import org.hage.monitoring.visualization.storage.element.VisualDataStorage;
import org.hage.monitoring.visualization.storage.mongodb.config.MongoDBConfig;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;


/**
 * @author AGH AgE Team
 */
public class VisualDataStorageMongo implements VisualDataStorage {

    private String label;

    private StorageDescription storageDescription;

    private String tableName;

    private DB base = null;

    private DBCollection table = null;

    public VisualDataStorageMongo(StorageDescription storageDescription) {
        this.label = storageDescription.toString();
        this.storageDescription = storageDescription;
        StringBuilder sb = new StringBuilder();
        sb.append(storageDescription.getComputationType()).append("_")
                .append(storageDescription.getComputationInstance()).append("_")
                .append(storageDescription.getGathererId());
        this.tableName = sb.toString();
        try {
            base = MongoDBConfig.getMongoBase();
            table = base.getCollection(tableName);
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void save(VisualData data) {
        BasicDBObject document = new BasicDBObject();
        document.put("value", data.getData());
        document.put("timestamp", data.getTimestamp());
        table.insert(document);
    }


    @Override
    public List<VisualData> getYoungerThan(Date date) {
        BasicDBObject query = new BasicDBObject();
        query.put("timestamp", new BasicDBObject("$gt", date.getTime()));
        DBCursor cursor = table.find(query);
        List<VisualData> result = newLinkedList();
        while(cursor.hasNext()) {
            DBObject entry = cursor.next();
            Double value = (Double) entry.get("value");
            Long timestamp = (Long) entry.get("timestamp");
            result.add(new VisualData(timestamp, value));
        }

        return result;
    }

    @Override
    public StorageDescription getStorageDescription() {
        return storageDescription;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public Collection<VisualData> all() {
        DBCursor cursor = table.find();
        List<VisualData> result = newLinkedList();
        while(cursor.hasNext()) {
            DBObject entry = cursor.next();
            result.add(new VisualData((Long) entry.get("timestamp"), (Double) entry.get("value")));
        }
        return result;
    }

    @Override
    public VisualData get(String key) {
        // TODO: consider implement this method which returns value (VisualData object)
        // based on passed key. The key should be considered as timestamp is VisualData.
        // But so far, we don't need it.
        return null;
    }
}