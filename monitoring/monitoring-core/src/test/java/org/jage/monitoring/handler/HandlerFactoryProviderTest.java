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
package org.jage.monitoring.handler;


import org.jage.monitoring.config.FactoryMethodInvoker;
import org.jage.monitoring.observable.ObservableProvider;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.monitoring.observer.ObservedData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import rx.schedulers.Timestamped;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class HandlerFactoryProviderTest {

    @Mock
    private ObservableProvider observableProvider;

    @Mock
    private ObservableProvider observableProvider2;

    @Mock
    private Observable<Timestamped<Object>> observable;

    @Mock
    private Observable<Timestamped<Object>> observable2;

    @Mock
    private Observable<ObservedData> mergedObservable;

    @Mock
    private AbstractStatefulObserver abstractObserver;

    @Mock
    private AbstractStatefulObserver abstractObserver2;

    @Mock
    private FactoryMethodInvoker factoryMethodInvoker;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAndSubscribeHandlerBasedObservableOnObserversTest() {

        // given
        when(factoryMethodInvoker.invokeFactoryMethod(any(List.class), any(String.class), any(String.class), any(String.class))).thenReturn(mergedObservable);

        when(observableProvider.provideObservable()).thenReturn(observable);
        when(observableProvider2.provideObservable()).thenReturn(observable2);
        HandlerFactoryProvider hfp = new HandlerFactoryProvider("handlerName",
                                                                Arrays.asList(observableProvider, observableProvider2),
                                                                Arrays.asList(abstractObserver, abstractObserver2),
                                                                "class", "method");

        hfp.setFactoryMethodInvoker(factoryMethodInvoker);

        // when
        hfp.createAndSubscribeHandlerBasedObservableOnObservers();

        //then
        verify(observableProvider).provideObservable();
        verify(observableProvider2).provideObservable();
        verify(abstractObserver).addObservable(mergedObservable);
        verify(abstractObserver2).addObservable(mergedObservable);
    }
}
