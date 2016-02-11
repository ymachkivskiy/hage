package org.hage.examples.hage.distrconf;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.execution.agent.SimpleAgent;

import javax.inject.Inject;

@Slf4j
public class LightAgent extends SimpleAgent {

    @Inject
    private SomeFooComponent component;

    public LightAgent(AgentAddress address) {
        super(address);
    }

    @Inject
    public LightAgent(AgentAddressSupplier supplier) {
        super(supplier);
    }


    @Override
    public void step() {
        log.info("agent {} perform step", getAddress());
        component.processMessage("hello from " + getAddress());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
