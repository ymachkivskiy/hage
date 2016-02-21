package org.hage.platform.component.execution.workplace.manager;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.component.execution.core.ExecutionCore;
import org.hage.platform.component.execution.workplace.Workplace;
import org.hage.platform.component.execution.workplace.WorkplaceEnvironment;
import org.hage.platform.component.simulation.structure.ComputationStructureConfigurator;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * The core component that manages threaded units of work called "workplaces".<p>
 *
 * @author AGH AgE Team
 */
public interface WorkplaceManager extends ExecutionCore, WorkplaceEnvironment, ComputationStructureConfigurator {

    String SERVICE_NAME = WorkplaceManager.class.getSimpleName();

    /**
     * Returns the workplace with the given address.
     *
     * @param address Address of the workplace.
     * @return a workplace or {@code null} if not found
     */
    @CheckForNull
    Workplace getLocalWorkplace(@Nonnull AgentAddress address);

    /**
     * Returns all workplaces in this manager.
     *
     * @return the list of workplaces.
     */
    @Nonnull
    List<Workplace> getLocalWorkplaces();

}
