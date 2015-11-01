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
package org.hage.monitoring;


import com.google.common.base.Suppliers;
import org.hage.monitoring.config.TypeSafeConfig;
import org.hage.monitoring.config.XmlConfig;
import org.hage.monitoring.observer.AbstractStatefulObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;
import rx.schedulers.Timestamped;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MonitoringTest {

    @Mock
    private Observer<Timestamped<Object>> observer;

    @Mock
    private AbstractStatefulObserver obs, obs2, obs3;

    @Mock
    private XmlConfig xmlConfig;

    @Mock
    private TypeSafeConfig typeSafeConfig;

    private TestScheduler testScheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testScheduler = Schedulers.test();
    }

    @Test
    public void getAllObserversWithXmlAndTS() {

        // given
        when(xmlConfig.getUsedObservers()).thenReturn(Sets.newSet(obs, obs2));
        when(typeSafeConfig.getUsedObservers()).thenReturn(Sets.newSet(obs2, obs3));
        Monitoring monitoring = new Monitoring(xmlConfig, typeSafeConfig);

        // when
        Collection<AbstractStatefulObserver> observers = monitoring.getAllObservers();

        // then
        assertEquals(3, observers.size());
    }

    @Test
    public void getAllObserversWithXml() {

        // given
        when(xmlConfig.getUsedObservers()).thenReturn(Sets.newSet(obs, obs2));
        Monitoring monitoring = new Monitoring(xmlConfig);

        // when
        Collection<AbstractStatefulObserver> observers = monitoring.getAllObservers();

        // then
        assertEquals(2, observers.size());
    }

    @Test
    public void getAllObserversWithTS() {

        // given
        when(typeSafeConfig.getUsedObservers()).thenReturn(Sets.newSet(obs2, obs3));
        Monitoring monitoring = new Monitoring(typeSafeConfig);

        // when
        Collection<AbstractStatefulObserver> observers = monitoring.getAllObservers();

        // then
        assertEquals(2, observers.size());
    }

    @Test
    public void shouldCallTwiceObservableFromMethodCreateObservableFromSupplier() throws InterruptedException {

        //when
        Observable<Timestamped<Object>> observable = Monitoring.createObservableFromSupplier(Suppliers.ofInstance(new Long(1)), 50, testScheduler);
        observable.subscribe(observer);
        testScheduler.advanceTimeTo(100, TimeUnit.MILLISECONDS);
        //then
        verify(observer, times(2)).onNext(Matchers.any(Timestamped.class));
    }

    @Test
    public void shouldCallOnceObservableFromMethodCreateObservableFromSupplier() throws InterruptedException {

        //when
        Observable<Timestamped<Object>> observable = Monitoring.createObservableFromSupplier(Suppliers.ofInstance(new Long(1)), 50, testScheduler);
        observable.subscribe(observer);
        testScheduler.advanceTimeTo(50, TimeUnit.MILLISECONDS);

        //then
        verify(observer).onNext(Matchers.any(Timestamped.class));
    }

    @Test
    public void shouldStopProduceDataBeforeCreationOfAnyData() throws InterruptedException {

        //when
        Observable<Timestamped<Object>> observable = Monitoring.createObservableFromSupplier(Suppliers.ofInstance(new Long(1)), 100, testScheduler);
        observable.subscribe(observer);
        testScheduler.advanceTimeTo(60, TimeUnit.MILLISECONDS);

        //then
        verify(observer, never()).onNext(Matchers.any(Timestamped.class));
    }
}