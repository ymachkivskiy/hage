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
package org.jage.monitoring.test.functional;


import org.jage.monitoring.Monitoring;
import org.jage.monitoring.config.ExecutorProvider;
import org.jage.monitoring.config.ExecutorShutdownCaller;
import org.jage.monitoring.config.RxTestSchedulerProvider;
import org.jage.monitoring.config.XmlConfig;
import org.jage.monitoring.handler.HandlerFactoryProvider;
import org.jage.monitoring.observable.ObservableProvider;
import org.jage.monitoring.observer.ObserverUnderTest;
import org.jage.monitoring.supplier.RandomSupplier;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.builder.ComponentBuilder;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.junit.Before;
import org.junit.Test;
import rx.schedulers.TestScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.jage.platform.component.builder.ConfigurationBuilder.Configuration;
import static org.junit.Assert.assertEquals;


public class FunctionalTest {

    private ComponentBuilder componentBuilder;

    @Before
    public void setUp() {
        componentBuilder = Configuration()
                .Component("executorProvider", ExecutorProvider.class)
                .Component("executorShutdownCaller", ExecutorShutdownCaller.class)
                .Component("rxSchedulerProvider", RxTestSchedulerProvider.class)
                .Component("random1", RandomSupplier.class, false)
                .Component("random2", RandomSupplier.class, false)
                .Component("observer", ObserverUnderTest.class);
    }

    /**
     * Test with two observables and one basic handler.
     *
     * @throws InterruptedException
     */
    @Test
    public void oneBasicHandlerWithTwoObservables() throws InterruptedException {

        List<IComponentDefinition> components = componentBuilder
                .Component("monitoring", Monitoring.class)
                .withConstructorArgRef("xmlConfig")
                .Component("xmlConfig", XmlConfig.class)
                .withConstructorArgRef("handlerList")
                .List("handlerList")
                .withItemRef("handler1")
                .Component("random1ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random1")
                .withConstructorArg(Long.class, "50")
                .Component("random2ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random2")
                .withConstructorArg(Long.class, "50")
                .List("observableProviderList", ObservableProvider.class)
                .withItemRef("random1ObservableProvider")
                .withItemRef("random2ObservableProvider")
                .List("observersList")
                .withItemRef("observer")
                .Component("handler1", HandlerFactoryProvider.class)
                .withConstructorArg(String.class, "handler1")
                .withConstructorArgRef("observableProviderList")
                .withConstructorArgRef("observersList")
                .withConstructorArg(String.class,
                                    "org.jage.monitoring.handler.BasicHandlerFactory")
                .withConstructorArg(String.class, "create").build();

        PicoComponentInstanceProvider ioc = new PicoComponentInstanceProvider();
        for(IComponentDefinition def : components) {
            ioc.addComponent(def);
        }
        ioc.getComponents(IStatefulComponent.class);

        TestScheduler scheduler = (TestScheduler) ioc.getComponent(RxTestSchedulerProvider.class).getScheduler();
        scheduler.advanceTimeTo(300, TimeUnit.MILLISECONDS);
        ioc.getComponent(Monitoring.class).finish();

        ObserverUnderTest observer = (ObserverUnderTest) ioc.getComponent("observer");
        assertEquals(12, observer.getNextCount());
        assertEquals(1, observer.getCompleteCount());

    }

    /**
     * Test with two handlers.
     *
     * @throws InterruptedException
     */
    @Test
    public void twoBasicHandlersWithOneObservableEach()
            throws InterruptedException {

        List<IComponentDefinition> components = componentBuilder
                .Component("monitoring", Monitoring.class)
                .withConstructorArgRef("xmlConfig")
                .Component("xmlConfig", XmlConfig.class)
                .withConstructorArgRef("handlerList")
                .List("handlerList")
                .withItemRef("handler1")
                .withItemRef("handler2")
                .Component("random1ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random1")
                .withConstructorArg(Long.class, "50")
                .Component("random2ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random2")
                .withConstructorArg(Long.class, "50")
                .List("observableProviderList1", ObservableProvider.class)
                .withItemRef("random1ObservableProvider")
                .List("observableProviderList2", ObservableProvider.class)
                .withItemRef("random2ObservableProvider")
                .List("observersList")
                .withItemRef("observer")
                .Component("handler1", HandlerFactoryProvider.class)
                .withConstructorArg(String.class, "handler1")
                .withConstructorArgRef("observableProviderList1")
                .withConstructorArgRef("observersList")
                .withConstructorArg(String.class,
                                    "org.jage.monitoring.handler.BasicHandlerFactory")
                .withConstructorArg(String.class, "create")
                .Component("handler2", HandlerFactoryProvider.class)
                .withConstructorArg(String.class, "handler2")
                .withConstructorArgRef("observableProviderList2")
                .withConstructorArgRef("observersList")
                .withConstructorArg(String.class,
                                    "org.jage.monitoring.handler.BasicHandlerFactory")
                .withConstructorArg(String.class, "create")
                .build();

        PicoComponentInstanceProvider ioc = new PicoComponentInstanceProvider();
        for(IComponentDefinition def : components) {
            ioc.addComponent(def);
        }
        ioc.getComponents(IStatefulComponent.class);

        TestScheduler scheduler = (TestScheduler) ioc.getComponent(RxTestSchedulerProvider.class).getScheduler();
        scheduler.advanceTimeTo(300, TimeUnit.MILLISECONDS);
        ioc.getComponent(Monitoring.class).finish();
        ObserverUnderTest observer = (ObserverUnderTest) ioc.getComponent("observer");
        assertEquals(12, observer.getNextCount());
        assertEquals(1, observer.getCompleteCount());
    }

    /**
     * Test with avg handler.
     *
     * @throws InterruptedException
     */
    @Test
    public void oneAvgHandlerWithTwoObservables() throws InterruptedException {

        List<IComponentDefinition> components = componentBuilder
                .Component("monitoring", Monitoring.class)
                .withConstructorArgRef("xmlConfig")
                .Component("xmlConfig", XmlConfig.class)
                .withConstructorArgRef("handlerList")
                .List("handlerList")
                .withItemRef("handler1")
                .Component("random1ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random1")
                .withConstructorArg(Long.class, "50")
                .Component("random2ObservableProvider",
                           ObservableProvider.class)
                .withConstructorArgRef("random2")
                .withConstructorArg(Long.class, "50")
                .List("observableProviderList", ObservableProvider.class)
                .withItemRef("random1ObservableProvider")
                .withItemRef("random2ObservableProvider")
                .List("observersList")
                .withItemRef("observer")
                .Component("handler1", HandlerFactoryProvider.class)
                .withConstructorArg(String.class, "handler1")
                .withConstructorArgRef("observableProviderList")
                .withConstructorArgRef("observersList")
                .withConstructorArg(String.class,
                                    "org.jage.monitoring.handler.AvgHandlerFactory")
                .withConstructorArg(String.class, "create").build();

        PicoComponentInstanceProvider ioc = new PicoComponentInstanceProvider();
        for(IComponentDefinition def : components) {
            ioc.addComponent(def);
        }
        ioc.getComponents(IStatefulComponent.class);

        TestScheduler scheduler = (TestScheduler) ioc.getComponent(RxTestSchedulerProvider.class).getScheduler();
        scheduler.advanceTimeTo(300, TimeUnit.MILLISECONDS);
        ioc.getComponent(Monitoring.class).finish();
        ObserverUnderTest observer = (ObserverUnderTest) ioc.getComponent("observer");
        assertEquals(6, observer.getNextCount());
        assertEquals(1, observer.getCompleteCount());
    }
}