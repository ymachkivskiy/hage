package org.hage.examples.hage.distrconf;

import org.hage.address.agent.AgentAddress;
import org.hage.address.agent.AgentAddressSupplier;
import org.hage.agent.SimpleAgent;

import javax.inject.Inject;

public class HeavyAgent extends SimpleAgent {

    @Inject
    private SomeFooComponent component;

    @Inject
    public HeavyAgent(AgentAddressSupplier supplier) {
        super(supplier);
    }

    public HeavyAgent(AgentAddress address) {
        super(address);
    }

    @Override
    public void step() {
        log.info("agent {} perform step", getAddress());
        component.processMessage("hello from " + getAddress());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
