package org.hage.platform.config.distribution.division;

import lombok.Data;
import org.hage.platform.config.distribution.endpoint.AllocationPart;

import java.util.List;

@Data
public class Allocation {
    private final List<AllocationPart> allocationParts;
}
