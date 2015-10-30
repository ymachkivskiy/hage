package org.jage.examples.hage.distrconf;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAgent;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

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
