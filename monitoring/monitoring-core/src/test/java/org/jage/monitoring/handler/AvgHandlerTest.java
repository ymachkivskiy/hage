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


import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.monitoring.observer.ObservedData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import rx.schedulers.Timestamped;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class AvgHandlerTest {

    @Mock
    AbstractStatefulObserver observer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void computeNoneAvg() throws InterruptedException {

        //given
        List<Observable<Timestamped<Object>>> observableList = Arrays.asList(
                Observable.from(new Object[]{}).timestamp(),
                Observable.from(new Object[]{10L, 20L, 30L}).timestamp()
        );

        //when
        Observable<ObservedData> observable = AvgHandlerFactory.create(observableList, "name");
        observable.subscribe(observer);

        //then
        verify(observer, never()).onNext(Matchers.any(ObservedData.class));
        verify(observer).onCompleted();

    }

    @Test
    public void computeThreeAvgs() throws InterruptedException {

        //given
        List<Observable<Timestamped<Object>>> observableList = Arrays.asList(
                Observable.from(new Object[]{1L, 2L, 3L}).timestamp(),
                Observable.from(new Object[]{10L, 20L, 30L}).timestamp()
        );

        //when
        Observable<ObservedData> observable = AvgHandlerFactory.create(observableList, "name");
        observable.subscribe(observer);

        //then
        verify(observer, times(3)).onNext(Matchers.any(ObservedData.class));
        verify(observer).onCompleted();

    }

    @Test
    public void computeThreeAvgsFromDifferentLengthObservables() throws InterruptedException {

        //given
        List<Observable<Timestamped<Object>>> observableList = Arrays.asList(
                Observable.from(new Object[]{1L, 2L, 3L, 4L, 5L}).timestamp(),
                Observable.from(new Object[]{10L, 20L, 30L}).timestamp()
        );

        //when
        Observable<ObservedData> observable = AvgHandlerFactory.create(observableList, "name");
        observable.subscribe(observer);

        //then
        verify(observer, times(3)).onNext(Matchers.any(ObservedData.class));
        verify(observer).onCompleted();

    }
}
