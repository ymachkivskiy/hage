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
 * Created: 2012-02-09
 * $Id$
 */

package org.hage.examples.distributed;


import org.hage.address.node.NodeAddressSupplier;
import org.hage.bus.EventBus;
import org.hage.platform.component.exception.ComponentException;
import org.hage.services.core.CoreComponent;
import org.hage.services.core.CoreComponentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This is an example component that uses the communication service to send and receive messages. It simply tries to
 * send 10 broadcast messages and checks its delivery queue the same number of times.
 *
 * @author AGH AgE Team
 */
public class CommunicatingCoreComponent implements CoreComponent {

    private static final Logger log = LoggerFactory.getLogger(CommunicatingCoreComponent.class);

    //private ComponentAddress myAddress;

    private static final String NAME = "communicatingCoreComponent";

    //@Inject
    //private CommunicationService communicationService;

    @Inject
    private NodeAddressSupplier addressProvider;

    @Inject
    private EventBus eventBus;

    @Override
    public void init() throws ComponentException {
        //	myAddress = new DefaultComponentAddress(NAME, addressProvider.get());
    }

    @Override
    public boolean finish() throws ComponentException {
        // Empty
        return false;
    }

    @Override
    public void start() throws ComponentException {
        eventBus.post(new CoreComponentEvent(CoreComponentEvent.Type.STARTING));

        for(int i = 0; i < 10; i++) {
            //		final Header<ComponentAddress> header = new DefaultHeader<>(myAddress,
            //				Selectors.allAddressesOfComponents(NAME));
            //		final Message<ComponentAddress, String> message = new DefaultMessage<>(header, "Hello world!");

            //		log.info("Sending message: {}.", message);
            //		communicationService.send(message);

            try {
                Thread.sleep(2000);
            } catch(final InterruptedException e) {
                log.error("Interrupted.", e);
            }

            //		final Message<ComponentAddress, Serializable> receivedMessage = communicationService.receive(myAddress);
            //		log.info("Received message: {}.", receivedMessage);
        }

        eventBus.post(new CoreComponentEvent(CoreComponentEvent.Type.STOPPED));
    }

    @Override
    public void pause() {
        // Empty
    }

    @Override
    public void resume() {
        // Empty
    }

    @Override
    public void stop() {
        // Empty
    }

    @Override
    public void teardownConfiguration() {
        // Empty
    }

}
