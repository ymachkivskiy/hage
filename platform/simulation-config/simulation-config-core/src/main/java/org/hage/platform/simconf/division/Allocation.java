package org.hage.platform.simconf.division;

import lombok.Data;
import org.hage.platform.simconf.endpoint.AllocationPart;

import java.util.List;

@Data
public class Allocation {
    private final List<AllocationPart> allocationParts;
}
