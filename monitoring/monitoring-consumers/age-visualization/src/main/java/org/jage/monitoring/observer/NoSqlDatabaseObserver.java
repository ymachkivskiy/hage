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
package org.jage.monitoring.observer;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.jage.monitoring.config.ComputationInstanceProvider;
import org.jage.monitoring.config.DefaultComputationInstanceProvider;
import org.jage.monitoring.config.ExecutorProvider;
import org.jage.monitoring.observer.utils.UrlFormatter;
import org.jage.monitoring.visualization.storage.mongodb.config.MongoDBConfig;
import org.jage.platform.component.provider.IComponentInstanceProvider;

import javax.inject.Inject;


/**
 * Data observer which stores data in NoSQL database.
 *
 * @author AGH AgE Team
 */
public class NoSqlDatabaseObserver extends AbstractStatefulObserver {

    private String computationInstance;

    @Inject
    private ComputationInstanceProvider computationInstanceProvider;
    private IComponentInstanceProvider componentInstanceProvider;
    private DB base = null;
    private String host;
    private String schema;
    private String computationType;
    private String tableName;

    public NoSqlDatabaseObserver(String url, IComponentInstanceProvider provider, ExecutorProvider executorProvider) {
        this(url);
        this.componentInstanceProvider = provider;
        this.executorProvider = executorProvider;
    }

    public NoSqlDatabaseObserver(String url) {
        this.host = UrlFormatter.getHostFromUrl(url);
        this.schema = UrlFormatter.getSchemaFromUrl(url);
        this.computationType = UrlFormatter.getComputationTypeFromUrl(url);
    }

    @Override
    public void init() {
        super.init();
        if(computationInstanceProvider == null) {
            computationInstanceProvider = componentInstanceProvider
                    .getInstance(DefaultComputationInstanceProvider.class);
        }
        computationInstance = computationInstanceProvider
                .getComputationInstance().toString();
        StringBuilder tableNameSB = new StringBuilder();
        tableName = tableNameSB.append(computationType).append("_")
                .append(computationInstance).append("_").toString();

        base = MongoDBConfig.getMongoBase(host, schema);
    }

    @Override
    public void onNext(final ObservedData args) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                BasicDBObject document = new BasicDBObject();
                Object value = args.getData();
                if(value instanceof Number) {
                    value = ((Number) value).doubleValue();
                }
                document.put("value", value);
                document.put("timestamp", args.getTimestamp());
                DBCollection localTable = base.getCollection(tableName + args.getName());
                localTable.insert(document);
            }
        });
    }
}
