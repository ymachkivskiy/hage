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
 * File: MonitoringSimpleAgent.java
 * Created: 16-10-2012
 * Author: kamilk
 * $Id$
 */

package org.hage.examples.monitoring;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.agent.SimpleAgent;
import org.hage.property.PropertyGetter;
import org.hage.property.PropertySetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Random;


/**
 * @author AGH AgE Team
 */
public class MonitoringSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = 3L;

    private final Logger log = LoggerFactory.getLogger(MonitoringSimpleAgent.class);

    private final Random random = new Random();

    private int stepNo = 1;

    private int chanceToMeetUfo = 0;

    private double radiationFromUfo = 0d;

    private int alienAbductions = 0;

    public MonitoringSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public MonitoringSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @PropertyGetter(propertyName = "chanceToMeetUfo")
    public int getChanceToMeetUfo() {
        return chanceToMeetUfo;
    }

    @PropertySetter(propertyName = "chanceToMeetUfo")
    public void setChanceToMeetUfo(int chanceToMeetUfo) {
        this.chanceToMeetUfo = chanceToMeetUfo;
    }

    @PropertyGetter(propertyName = "radiationFromUfo")
    public double getRadiationFromUfo() {
        return radiationFromUfo;
    }

    @PropertySetter(propertyName = "radiationFromUfo")
    public void setRadiationFromUfo(double radiationFromUfo) {
        this.radiationFromUfo = radiationFromUfo;
    }

    @PropertyGetter(propertyName = "alienAbductions")
    public int getAlienAbductions() {
        return alienAbductions;
    }

    @PropertySetter(propertyName = "alienAbductions")
    public void setAlienAbductions(int alienAbductions) {
        this.alienAbductions = alienAbductions;
    }

    @Override
    public void step() {
        chanceToMeetUfo += random.nextInt(10);
        radiationFromUfo += chanceToMeetUfo / Math.abs(random.nextGaussian());
        alienAbductions += random.nextInt(5);
        log.info("{} performed step no. {} ", getAddress(), stepNo);
        stepNo++;
        try {
            Thread.sleep(200);
        } catch(final InterruptedException e) {
            log.error("Interrupted", e);
        }
    }
}
