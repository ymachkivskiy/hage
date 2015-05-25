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
 * Created: 2009-05-18
 * $Id$
 */

package org.jage.workplace;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.bus.EventBus;
import org.jage.query.CollectionQuery;
import org.jage.services.core.CoreComponentEvent;
import org.jage.workplace.manager.WorkplaceManager;

import static org.jage.query.ValueFilters.lessThan;
import static org.jage.query.ValueSelectors.field;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Stop condition which stops workplaces when all of them perform fixed steps count. It gets in constructor number of
 * steps after which workplaces are stopped. In no number is given the default value is set.
 * <p>
 *
 * Important: with the version 2.6 semantics of this stop condition changed: now <emph>all</emph> of workplaces must
 * pass at least the provided number of steps for the computation to stop.
 *
 * @author AGH AgE Team
 */
public class FixedStepCountStopCondition implements IStopCondition {

	private static Logger log = LoggerFactory.getLogger(FixedStepCountStopCondition.class);

	private static final long DEFAULT_STEP_COUNT = 10;

	private final long stepCount;

	private final AtomicBoolean alreadySatisfied = new AtomicBoolean(false);

	private final ListeningScheduledExecutorService executor = MoreExecutors.listeningDecorator(
			Executors.newSingleThreadScheduledExecutor());

	private ScheduledFuture<?> future;

	@Inject private EventBus eventBus;

	@Inject private WorkplaceManager workplaceManager;

	/**
	 * Creates a new step counting condition with the default maximum step count.
	 */
	@Inject
	public FixedStepCountStopCondition() {
		this(DEFAULT_STEP_COUNT);
	}

	/**
	 * Creates a new step counting condition with the provided maximum step count.
	 *
	 * @param stepCount
	 *            a number of steps before this stop condition is satisfied, must be greater than zero.
	 */
	@Inject
	public FixedStepCountStopCondition(final Long stepCount) {
		checkNotNull(stepCount);
		checkArgument(stepCount > 0,
				"FixedStepCountStopCondition: number of steps cannot be equal or less then zero. Given value: %s.",
				stepCount);
		this.stepCount = stepCount;
		log.info("Fixed step stop condition created with step set to: {}.", this.stepCount);
	}

	@Subscribe public void onCoreComponentEvent(@Nonnull final CoreComponentEvent event) {
		log.debug("Event: {}.", event);
		switch (event.getType()) {
			case STARTING:
				future = executor.scheduleWithFixedDelay(observer, 500, 500, TimeUnit.MILLISECONDS);
				break;
			case STOPPED:
				future.cancel(true);
				break;
		}

	}

	@Override
	public void init() {
		eventBus.register(this);
	}

	@Override
	public boolean finish() {
		executor.shutdown();
		return true;
	}

	private Runnable observer = new Runnable() {
		@Override public void run() {
			if (!alreadySatisfied.get() && shouldStop()) {
				log.info("The stop condition has been satisfied.");
				alreadySatisfied.set(true);
				eventBus.post(new StopConditionFulfilledEvent());
			}
		}
	};

	private boolean shouldStop() {
		final CollectionQuery<Workplace, Long> query = new CollectionQuery<>(SimpleWorkplace.class);

		final Collection<Long> results = query.matching("step", lessThan(stepCount))
		        .select(field("step")).execute(workplaceManager.getLocalWorkplaces());
		log.info("Q={}", results);
		return results.isEmpty();
	}

}
