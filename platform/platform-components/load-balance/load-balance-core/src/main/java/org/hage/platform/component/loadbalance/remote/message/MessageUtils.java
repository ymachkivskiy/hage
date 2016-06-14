package org.hage.platform.component.loadbalance.remote.message;

import org.hage.platform.component.execution.monitor.DynamicExecutionInfo;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType.*;

public class MessageUtils {

    public static LoadBalancerRemoteMessage ackMessage() {
        return new LoadBalancerRemoteMessage(ACK, null);
    }

    public static LoadBalancerRemoteMessage requestForStatsMsg() {
        return new LoadBalancerRemoteMessage(RQ__PROVIDE_NODE_STATS, null);
    }

    public static LoadBalancerRemoteMessage respondWithStatsMsg(DynamicExecutionInfo dynamicExecutionInfo) {
        return new LoadBalancerRemoteMessage(ACK, new LoadBalanceData(dynamicExecutionInfo, emptyList()));
    }

    public static LoadBalancerRemoteMessage unitsRelocationOrderMsg(List<UnitRelocationOrder> unitRelocationOrders) {
        return new LoadBalancerRemoteMessage(RQ__UNITS_RELOCATION_ORDER, new LoadBalanceData(null, unitRelocationOrders));
    }

}
