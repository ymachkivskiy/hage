package org.hage.platform.component.simulationconfig.division;

import lombok.Data;
import org.hage.platform.component.simulationconfig.endpoint.AllocationPart;

import java.util.List;

@Data
public class Allocation {
    private final List<AllocationPart> allocationParts;
}
