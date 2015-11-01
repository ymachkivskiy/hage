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
package org.hage.monitoring.observer;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ObserverTest {

    private Observable<ObservedData> observable;
    private Observable<ObservedData> observable2;

    @Before
    public void setUp() {
        observable = Observable.from(new ObservedData[]{new ObservedData("name", 12345L, 1), new ObservedData("name", 12345L, 2), new ObservedData("name", 12345L, 3)});
        observable2 = Observable.from(new ObservedData[]{new ObservedData("name", 12345L, 4), new ObservedData("name", 12345L, 5)});
    }

    @Test
    public void observerProcessesItemsFromOneObservable() throws InterruptedException {

        // given
        AbstractStatefulObserver observer = mock(AbstractStatefulObserver.class);

        // when
        observable.subscribe(observer);

        // then
        verify(observer, times(3)).onNext(Mockito.any(ObservedData.class));
        verify(observer).onCompleted();
    }

    @Test
    public void observerProcessesItemsFromMergedObservables() throws InterruptedException {

        // given
        AbstractStatefulObserver observer = mock(AbstractStatefulObserver.class);
        Observable<ObservedData> merged = Observable.merge(observable, observable2);

        // when
        merged.subscribe(observer);

        // then
        verify(observer, times(5)).onNext(Mockito.any(ObservedData.class));
        verify(observer).onCompleted();
    }
}