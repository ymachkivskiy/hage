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
package org.hage.monitoring.config;


import org.hage.monitoring.Monitoring;
import org.hage.monitoring.observer.AbstractStatefulObserver;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class ExecutorShutdownCaller implements IStatefulComponent, Observer<Object> {

    private static final Logger log = LoggerFactory.getLogger(ExecutorShutdownCaller.class);

    @Inject
    private ExecutorProvider executorProvider;
    @Inject
    private Monitoring monitoring;

    @Override
    public void onCompleted() {
        executorProvider.getExecutor().shutdown();
    }

    @Override
    public void onError(Throwable e) {
        log.error("The following error occured:", e);
    }

    @Override
    public void onNext(Object t) {
        // Not used
    }

    @Override
    public void init() throws ComponentException {

        List<Observable<Object>> isCompletedObservables = new ArrayList<>();

        for(AbstractStatefulObserver observer : monitoring.getAllObservers()) {
            isCompletedObservables.add(observer.isCompleted());
        }

        Observable.merge(isCompletedObservables).subscribe(this);
    }

    @Override
    public boolean finish() throws ComponentException {
        return false;
    }

    public void setMonitoring(Monitoring monitoring) {
        this.monitoring = monitoring;
    }

}