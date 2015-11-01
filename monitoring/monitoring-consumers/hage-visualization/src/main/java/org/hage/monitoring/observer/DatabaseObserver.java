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
 * File: DatabaseDataForwarder.java
 * Created: 11-09-2012
 * Author: kamilk
 * $Id$
 */

package org.hage.monitoring.observer;


import org.hage.monitoring.config.ComputationInstanceProvider;
import org.hage.monitoring.config.DefaultComputationInstanceProvider;
import org.hage.monitoring.config.ExecutorProvider;
import org.hage.monitoring.observer.utils.UrlFormatter;
import org.hage.monitoring.persistence.config.HibernateConfiguration;
import org.hage.monitoring.persistence.service.PropertyService;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.provider.IComponentInstanceProvider;

import javax.inject.Inject;


/**
 * Data observer which stores data in SQL database.
 *
 * @author AGH AgE Team
 */
public class DatabaseObserver extends AbstractStatefulObserver {

    private PropertyService propertyService;

    private HibernateConfiguration hibernateConfiguration;

    @Inject
    private ComputationInstanceProvider computationInstanceProvider;
    private IComponentInstanceProvider componentInstanceProvider;
    private String url;
    private String user;
    private String password;
    private String computationType;
    private String computationInstance;

    public DatabaseObserver(String url, String user, String password, IComponentInstanceProvider provider, ExecutorProvider executorProvider) {
        this(url, user, password);
        this.componentInstanceProvider = provider;
        this.executorProvider = executorProvider;
    }

    public DatabaseObserver(String url, String user, String password) {
        super();
        this.url = url;
        this.user = user;
        this.password = password;
        this.computationType = UrlFormatter.getComputationTypeFromUrl(url);
    }

    @Override
    public void init() throws ComponentException {
        super.init();
        if(computationInstanceProvider == null) {
            computationInstanceProvider = componentInstanceProvider.getInstance(DefaultComputationInstanceProvider.class);
        }
        this.hibernateConfiguration = new HibernateConfiguration();
        this.hibernateConfiguration.initHibernateConfiguration(url, user, password);
        propertyService = new PropertyService(this.hibernateConfiguration);
        computationInstance = computationInstanceProvider.getComputationInstance();
    }

    @Override
    public boolean finish() throws ComponentException {
        super.finish();
        HibernateConfiguration.getFactory().close();
        return false;
    }

    @Override
    public void onCompleted() {
        log.info("{} has completed", getClass().getName());
    }

    @Override
    public void onNext(final ObservedData args) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                propertyService.saveProperty(args.getName(), computationInstance, computationType, args.getTimestamp(), args.getData());
            }
        });
    }
}
