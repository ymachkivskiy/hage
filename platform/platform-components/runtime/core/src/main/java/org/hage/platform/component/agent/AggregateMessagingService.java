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
 * Created: 2012-04-09
 * $Id$
 */

package org.hage.platform.component.agent;


import org.hage.address.agent.AgentAddress;
import org.hage.communication.message.Message;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.context.SendMessageActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The messaging service for the default {@link SimpleAggregate}. It just forwards messages as actions to the
 * {@link AggregateActionService}.
 *
 * @author AGH AgE Team
 * @since 2.6
 */
public class AggregateMessagingService {

    private static final Logger log = LoggerFactory.getLogger(AggregateMessagingService.class);

    private SimpleAggregate aggregate;

    private AggregateActionService actionService;

    /**
     * Sends the provided message.
     *
     * @param message the message to send.
     */
    public void sendMessage(final Message<AgentAddress, ?> message) {
        actionService.doAction(new SingleAction(message.getHeader().getReceiverSelector(),
                                                new SendMessageActionContext(message)));
    }

    /**
     * Sets the aggregate that owns this service instance.
     *
     * @param aggregate the owner.
     */
    public void setAggregate(final SimpleAggregate aggregate) {
        this.aggregate = checkNotNull(aggregate);
        actionService = aggregate.getActionService();
    }
}
