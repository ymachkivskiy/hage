#set($symbol_pound='#')
        #set($symbol_dollar='$')
        #set($symbol_escape='\' )
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
 * Created: 2011-03-15
 * ${symbol_dollar}Id: HelloWorldSimpleAgent.java 971 2011-09-27 17:24:51Z faber ${symbol_dollar}
 */

        package ${package};

        import javax.inject.Inject;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import org.jage.address.agent.AgentAddress;
        import org.jage.address.agent.AgentAddressSupplier;
        import org.jage.agent.SimpleAgent;


/**
 * This agent only logs basic information. It doesn't perform any other operations.
 * <p>
 * Make sure that logger configuration allows to log at info level from this class.
 *
 * @author AGH AgE Team
 */
public class HelloWorldSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = 3L;

    private final Logger log = LoggerFactory.getLogger(HelloWorldSimpleAgent.class);

    public HelloWorldSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public HelloWorldSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    /**
     * This method initialises the agent.
     * <p>
     * {@inheritDoc}
     *
     * @throws ComponentException Thrown when initialisation fails.
     * @see org.jage.agent.AbstractAgent${symbol_pound}init()
     */
    @Override
    public void init() {
        super.init();
        log.info("Initializing Hello World Simple Agent: {}", getAddress());
    }

    /**
     * In this method, the agent performs its work.
     * <p>
     * {@inheritDoc}
     *
     * @see org.jage.agent.SimpleAgent${symbol_pound}step()
     */
    @Override
    public void step() {
        log.info("{} says Hello World from {}", getAddress(), getParentAddress());
        try {
            Thread.sleep(200);
        } catch(InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    /**
     * This method is called when the agent is to be removed (e.g. when the system is shutting down).
     * <p>
     * {@inheritDoc}
     *
     * @see org.jage.agent.AbstractAgent${symbol_pound}finish()
     */
    @Override
    public boolean finish() {
        log.info("Finishing Hello World Simple Agent: {}", getAddress());
        return true;
    }

}
