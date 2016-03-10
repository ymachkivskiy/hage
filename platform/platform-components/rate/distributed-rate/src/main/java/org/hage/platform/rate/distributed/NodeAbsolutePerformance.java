package org.hage.platform.rate.distributed;

import com.google.common.primitives.UnsignedInteger;
import lombok.Data;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.rate.local.measure.PerformanceRate;
import org.hage.util.proportion.Countable;

import static com.google.common.primitives.UnsignedInteger.valueOf;

@Data
public class NodeAbsolutePerformance implements Countable {

    private final NodeAddress nodeAddress;
    private final PerformanceRate performanceRate;

    @Override
    public UnsignedInteger getCount() {
        return valueOf(performanceRate.getRate());
    }

}
