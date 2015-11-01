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
package org.hage.monitoring.supplier.test;


import org.hage.monitoring.Monitoring;
import org.hage.monitoring.config.ExecutorProvider;
import org.hage.monitoring.config.ExecutorShutdownCaller;
import org.hage.monitoring.config.RxTestSchedulerProvider;
import org.hage.monitoring.config.TypeSafeConfig;
import org.hage.monitoring.observer.AbstractStatefulObserver;
import org.hage.monitoring.observer.ObserverUnderTest;
import org.hage.monitoring.supplier.RandomSupplier;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.builder.ComponentBuilder;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.component.pico.PicoComponentInstanceProvider;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import rx.schedulers.TestScheduler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hage.platform.component.builder.ConfigurationBuilder.Configuration;
import static org.junit.Assert.assertEquals;


//TODO Fix this test
@Ignore
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
                .Component("observer", ObserverUnderTest.class, true);
    }

    /**
     * Test with TypeSafe configuration way of monitoring.
     *
     * @throws InterruptedException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    @Test
    public void typesafeConfigurationWay() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        List<IComponentDefinition> components = componentBuilder
                .Component("monitoring", Monitoring.class)
                .withConstructorArgRef("typesafe")
                .Component("typesafe", TypeSafeConfig.class)
                .withConstructorArg(String.class, "testMonitoring").build();

        PicoComponentInstanceProvider ioc = new PicoComponentInstanceProvider();
        for(IComponentDefinition def : components) {
            ioc.addComponent(def);
        }
        ioc.getComponents(IStatefulComponent.class);

        TestScheduler scheduler = (TestScheduler) ioc.getComponent(RxTestSchedulerProvider.class).getScheduler();
        scheduler.advanceTimeTo(300, TimeUnit.MILLISECONDS);

        Monitoring monitoring = ioc.getComponent(Monitoring.class);
        monitoring.finish();
        ObserverUnderTest observer = extractTestObserver(monitoring);

        assertEquals(18, observer.getNextCount());
        assertEquals(1, observer.getCompleteCount());
    }

    private ObserverUnderTest extractTestObserver(Monitoring monitoring)
            throws NoSuchFieldException, IllegalAccessException {
        Field tscField = monitoring.getClass().getDeclaredField("typeSafeConfig");
        tscField.setAccessible(true);
        TypeSafeConfig typeSafeConfig = (TypeSafeConfig) tscField.get(monitoring);

        Field observerMapField = typeSafeConfig.getClass().getDeclaredField("observerMap");
        observerMapField.setAccessible(true);
        Map<String, AbstractStatefulObserver> observerMap = (Map<String, AbstractStatefulObserver>) observerMapField.get(typeSafeConfig);
        return (ObserverUnderTest) observerMap.get("observertest");
    }

    /**
     * Test of using <code>ref</code> supplier type in TypeSafe file.
     *
     * @throws InterruptedException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Test
    public void typesafeConfigurationWayWithRefSupplier() throws InterruptedException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<IComponentDefinition> components = componentBuilder
                .Component("monitoring", Monitoring.class)
                .withConstructorArgRef("typesafe")
                .Component("typesafe", TypeSafeConfig.class)
                .withConstructorArg(String.class, "testRef").build();

        PicoComponentInstanceProvider ioc = new PicoComponentInstanceProvider();
        for(IComponentDefinition def : components) {
            ioc.addComponent(def);
        }
        ioc.getComponents(IStatefulComponent.class);

        TestScheduler scheduler = (TestScheduler) ioc.getComponent(RxTestSchedulerProvider.class).getScheduler();
        scheduler.advanceTimeTo(300, TimeUnit.MILLISECONDS);
        Monitoring monitoring = ioc.getComponent(Monitoring.class);
        monitoring.finish();
        ObserverUnderTest observer = extractTestObserver(monitoring);

        assertEquals(6, observer.getNextCount());
        assertEquals(1, observer.getCompleteCount());

    }
}
