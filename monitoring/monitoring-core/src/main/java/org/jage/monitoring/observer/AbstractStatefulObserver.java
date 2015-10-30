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


import org.jage.monitoring.config.ExecutorProvider;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * Abstract base class for all Observers used in monitoring.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractStatefulObserver implements Observer<ObservedData>, ICompletable, IStatefulComponent {

    protected static final Logger log = LoggerFactory.getLogger(AbstractStatefulObserver.class);

    @Inject
    protected ExecutorProvider executorProvider;
    protected ExecutorService executor;
    private List<Observable<ObservedData>> observables;
    private Subject<Object, Object> isCompleted;

    public AbstractStatefulObserver() {
        observables = new ArrayList<>();
        isCompleted = ReplaySubject.create();
    }

    @Override
    public void init() throws ComponentException {
        executor = executorProvider.getExecutor();
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    @Override
    public void onCompleted() {
        log.info("{} has completed", getClass().getName());
        isCompleted.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        log.error("The following error occured:", e);
    }

    @Override
    public Observable<Object> isCompleted() {
        return isCompleted;
    }

    /**
     * Adds argument to list of observables.
     *
     * @param observable
     */
    public void addObservable(Observable<ObservedData> observable) {
        observables.add(observable);
    }

    /**
     * Returns list of elements which would be observed by this observer.
     *
     * @return list of observables.
     */
    public List<Observable<ObservedData>> getObservables() {
        return observables;
    }
}