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


import com.google.common.base.Splitter;
import com.mongodb.DB;
import org.hage.monitoring.visualization.storage.StorageDescription;
import org.hage.monitoring.visualization.storage.StorageLoader;
import org.hage.monitoring.visualization.storage.mongodb.config.MongoDBConfig;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;


/**
 * @author AGH AgE Team
 */
public class StorageLoaderMongo implements StorageLoader {

    @Override
    public Collection<StorageDescription> loadSavedStorageDescriptions() {
        Collection<StorageDescription> result = newHashSet();
        try {
            DB mongoBase = MongoDBConfig.getMongoBase();
            Set<String> collectionNames = mongoBase.getCollectionNames();
            for(String name : collectionNames) {
                if(name.contains("_")) {
                    Iterator<String> splited = Splitter.on("_").split(name).iterator();
                    StorageDescription sd = new StorageDescription(splited.next(), splited.next(), splited.next());
                    result.add(sd);
                }
            }
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }

}
