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
package org.hage.monitoring.visualization.storage.mongodb.config;


import com.mongodb.DB;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;


/**
 * @author AGH AgE Team
 */
public class MongoDBConfig {

    public static final String host = "localhost";
    public static final String schema = "age";
    public static final int port = 27017;
    private static MongoClient mongoClient = null;

    public static DB getMongoBase() throws UnknownHostException {
        if(mongoClient == null) {
            mongoClient = new MongoClient(host, port);
        }
        return mongoClient.getDB(schema);
    }

    public static DB getMongoBase(String host, String schema) {

        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(host, 27017);
        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
        return mongoClient.getDB(schema);
    }
}
