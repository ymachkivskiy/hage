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
 * Created: 2012-04-20
 * $Id$
 */

package org.hage.examples.multiworkplace;


import org.hage.examples.migration.CrawlingSimpleAgent;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.communication.message.Message;
import org.hage.platform.component.action.AgentActions;
import org.hage.platform.component.agent.AgentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.hage.platform.communication.message.Messages.newMessageToParent;


/**
 * This agent should be located just beneath a workplace.
 *
 * @author AGH AgE Team
 */
public class MultiworkplaceSimpleAgent extends CrawlingSimpleAgent {

    private static final long serialVersionUID = 4L;
    private static Logger log = LoggerFactory.getLogger(MultiworkplaceSimpleAgent.class);
    private long sentMessagesCount = 0;

    public MultiworkplaceSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public MultiworkplaceSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @Override
    public void step() {
        try {
            final Message<AgentAddress, String> message = newMessageToParent(getAddress(), "DefaultMessage to parent");
            doAction(AgentActions.sendMessage(message));
            sentMessagesCount++;
        } catch(final AgentException e) {
            log.error("Could not send a message.", e);
        }

        step++;
        if((step + hashCode()) % 50 == 0) {
            considerMigration();
        }

        try {
            Thread.sleep(10);
        } catch(final InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    @Override
    public boolean finish() {
        super.finish();
        log.info("{}: Sent messages {} times.", getAddress().getFriendlyName(), sentMessagesCount);
        return true;
    }

    public long getSentMessagesCount() {
        return sentMessagesCount;
    }
}
