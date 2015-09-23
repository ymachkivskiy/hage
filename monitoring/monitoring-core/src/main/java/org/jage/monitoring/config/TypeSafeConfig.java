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
 * File: GathererLoader.java
 * Created: 03-10-2013
 * Author: Daniel
 * $Id$
 */

package org.jage.monitoring.config;


import com.google.common.base.Supplier;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.jage.monitoring.Monitoring;
import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.monitoring.observer.ObservedData;
import org.jage.monitoring.observer.ObserverProvider;
import org.jage.monitoring.supplier.SupplierProvider;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import rx.Observable;
import rx.schedulers.Timestamped;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;


/**
 * Creates and initializes suppliers, observables, observers and handlers components.
 *
 * @author AGH AgE Team
 */
public class TypeSafeConfig implements IStatefulComponent, IComponentInstanceProviderAware {

    public static final String observers = "observers";
    public static final String observables = "observables";
    public static final String handlers = "handlers";
    private static Map<String, ObserverProvider> observerProviders = newHashMap();
    private static Map<String, SupplierProvider> supplierProviders = newHashMap();
    @Inject
    private RxSchedulerProvider rxSchedulerProvider;
    private String config;
    private Map<String, Observable<Timestamped<Object>>> observableMap;
    private Map<String, AbstractStatefulObserver> observerMap;
    private Map<String, AbstractStatefulObserver> usedObserverMap;
    private IComponentInstanceProvider provider;
    private FactoryMethodInvoker factoryMethodInvoker = new FactoryMethodInvoker();

    static {
        for(ObserverProvider observerProvider : ServiceLoader.load(ObserverProvider.class)) {
            observerProviders.put(observerProvider.getType(), observerProvider);
        }

        for(SupplierProvider observableProvider : ServiceLoader.load(SupplierProvider.class)) {
            supplierProviders.put(observableProvider.getType(), observableProvider);
        }
    }

    public TypeSafeConfig(final String config) {
        this.config = config;
    }

    @Override
    public void init() throws ComponentException {
        loadFromConfig();
    }

    /**
     * Loads observables, observers and handlers based on passed name of typesafe configuration file.
     */
    public void loadFromConfig() {

        final Config c = ConfigFactory.load(config).getConfig("age.monitoring");
        observableMap = initObservables(c);
        usedObserverMap = newHashMap();
        observerMap = initObservers(c);
        initHandlers(c);
    }

    private Map<String, Observable<Timestamped<Object>>> initObservables(final Config c) {
        final Set<String> observableNames = new HashSet<>();
        for(final Entry<String, ConfigValue> entry : c.getObject(observables).entrySet()) {
            observableNames.add(entry.getKey());
        }
        final Map<String, Observable<Timestamped<Object>>> map = newHashMap();
        for(final String name : observableNames) {
            map.put(name,
                    Monitoring.createObservableFromSupplier(
                            makeSupplier(
                                    c.getConfig(observables + "." + name),
                                    name
                            ),
                            c.getConfig(observables + "." + name).getMilliseconds("rate"),
                            rxSchedulerProvider.getScheduler()
                    )
            );
        }
        return map;
    }

    private Map<String, AbstractStatefulObserver> initObservers(final Config c) {
        final Set<String> observerNames = new HashSet<>();
        for(final Entry<String, ConfigValue> entry : c.getObject(observers).entrySet()) {
            observerNames.add(entry.getKey());
        }
        final Map<String, AbstractStatefulObserver> map = newHashMap();
        for(final String name : observerNames) {
            map.put(name, makeObserver(c.getConfig(observers + "." + name), name));
        }
        return map;
    }

    private void initHandlers(final Config c) {
        final Set<String> handlerNames = new HashSet<>();
        for(final Entry<String, ConfigValue> entry : c.getObject(handlers).entrySet()) {
            handlerNames.add(entry.getKey());
        }
        for(final String name : handlerNames) {
            makeHandler(c.getConfig(handlers + "." + name), name);
        }
    }

    private Supplier<Object> makeSupplier(final Config c, final String name) {
        if(!supplierProviders.containsKey(c.getString("type"))) {
            throw new MonitoringException("Unknown supplier type " + name);
        }
        Supplier<Object> supplier = supplierProviders.get(c.getString("type")).create(c, provider);
        callInitOfComoponent(supplier);

        return supplier;
    }

    private AbstractStatefulObserver makeObserver(final Config c, final String name) {
        if(!observerProviders.containsKey(c.getString("type"))) {
            throw new MonitoringException("Unknown observer type " + name);
        }
        AbstractStatefulObserver observer = observerProviders.get(c.getString("type")).create(c, provider);
        callInitOfComoponent(observer);
        return observer;
    }

    private void makeHandler(final Config c, final String name) {

        final List<Observable<Timestamped<Object>>> observableList = newLinkedList();
        final List<String> observableNames = c.getStringList("observable");
        for(final String observableName : observableNames) {
            if(!observableMap.containsKey(observableName)) {
                throw new MonitoringException("Observable " + observableName + " cannot be found");
            }
            observableList.add(observableMap.get(observableName));
        }

        Observable<ObservedData> mergedObservable = factoryMethodInvoker.invokeFactoryMethod(observableList, name, c.getString("class"), c.getString("method"));

        final List<String> observerNames = c.getStringList("observer");
        for(final String observerName : observerNames) {
            if(!observerMap.containsKey(observerName)) {
                throw new MonitoringException("Observer " + observerName + " cannot be found");
            }
            AbstractStatefulObserver usedObserver = observerMap.get(observerName);
            usedObserver.addObservable(mergedObservable);
            usedObserverMap.put(observerName, usedObserver);
        }

    }

    private void callInitOfComoponent(final Object component) {
        if(component instanceof IStatefulComponent) {
            ((IStatefulComponent) component).init();
        }
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    /**
     * Returns observers defined in the TypeSafe configuration file but only these which are used by handler(s).
     *
     * @return collection of AbstractObserver
     */
    public Collection<AbstractStatefulObserver> getUsedObservers() {
        return usedObserverMap.values();
    }

    public void setFactoryMethodInvoker(FactoryMethodInvoker factoryMethodInvoker) {
        this.factoryMethodInvoker = factoryMethodInvoker;
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.provider = provider;
    }
}