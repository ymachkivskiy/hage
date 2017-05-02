package org.hage.platform.component.rate.cluster;

import com.google.common.primitives.UnsignedInteger;
import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.util.proportion.Countable;

import static com.google.common.primitives.UnsignedInteger.valueOf;

@Data
public class NodeAbsolutePerformance implements Countable {

    private final NodeAddress nodeAddress;
    private final PerformanceRate performanceRate;

    public UnsignedInteger getNormalizedCapacity() {
        return valueOf(performanceRate.getRate());
    }

}
