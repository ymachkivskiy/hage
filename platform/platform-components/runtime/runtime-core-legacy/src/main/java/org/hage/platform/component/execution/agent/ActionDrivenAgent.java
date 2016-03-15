package org.hage.platform.component.execution.agent;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.execution.action.Action;
import org.hage.platform.component.execution.action.preparers.IActionPreparer;
import org.hage.property.PropertyField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;


/**
 * This agent implementation relies on a {@link IActionPreparer} to provide its actual behavior.
 * <p>
 * <p>
 * Given the agent's state and environment, the {@link IActionPreparer} prepares an appropriate action, which is then
 * run by the agent.
 *
 * @author AGH AgE Team
 */
public class ActionDrivenAgent extends SimpleAgent {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(ActionDrivenAgent.class);
    @Inject
    private IActionPreparer<ActionDrivenAgent> actionPreparator;
    @PropertyField(propertyName = Properties.STEP)
    private long step = 0;

    public ActionDrivenAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public ActionDrivenAgent(final AgentAddressSupplier supplier) {
        super(supplier.get());
    }

    public long getStep() {
        return step;
    }

    @Override
    public void step() {
        try {
            final List<Action> actions = actionPreparator.prepareActions(this);
            log.debug("Submitting actions: {}", actions);
            doActions(actions);
        } catch(final AgentException e) {
            LOG.error("An exception occurred during the action call", e);
        }

        step++;
        notifyMonitorsForChangedProperties();
    }


    /**
     * ActionDrivenAgent properties.
     *
     * @author AGH AgE Team
     */
    public static class Properties {

        /**
         * The actual step of computation.
         */
        public static final String STEP = "step";
    }
}
