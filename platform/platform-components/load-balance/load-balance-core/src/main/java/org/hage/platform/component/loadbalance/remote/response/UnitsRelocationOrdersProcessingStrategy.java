package org.hage.platform.component.loadbalance.remote.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.component.loadbalance.rebalance.UnitRelocationOrder;
import org.hage.platform.component.loadbalance.remote.message.LoadBalanceData;
import org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType;
import org.hage.platform.component.loadbalance.remote.message.LoadBalancerRemoteMessage;
import org.hage.platform.component.runtime.migration.UnitMigrationPerformer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.util.executors.core.CoreBatchExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hage.platform.component.loadbalance.remote.message.LoadBalanceMessageType.RQ__UNITS_RELOCATION_ORDER;
import static org.hage.platform.component.loadbalance.remote.message.MessageUtils.ackMessage;
import static org.hage.util.CollectionUtils.nullSafe;

@Slf4j
@SingletonComponent
class UnitsRelocationOrdersProcessingStrategy implements ProcessingStrategy {

    @Autowired
    private UnitMigrationPerformer migrationPerformer;
    @Autowired
    private CoreBatchExecutor coreBatchExecutor;

    @Override
    public LoadBalanceMessageType getMessageType() {
        return RQ__UNITS_RELOCATION_ORDER;
    }

    @Override
    public LoadBalancerRemoteMessage process(LoadBalanceData message) {

        Collection<MigrationTask> migrationTasks = createMigrationTasksForOrders(nullSafe(message.getRelocationOrders()));
        coreBatchExecutor.executeAll(migrationTasks);

        return ackMessage();
    }

    private Collection<MigrationTask> createMigrationTasksForOrders(List<UnitRelocationOrder> unitRelocationOrders) {
        log.info("Got unit relocation orders {}", unitRelocationOrders);
        //todo : NOT IMPLEMENTED



        return Collections.emptyList();
    }


    @Data
    private class MigrationTask implements Runnable {
        private final NodeAddress targetNode;
        private final List<Position> positionsToMigrate;

        @Override
        public void run() {
            migrationPerformer.migrateUnits(positionsToMigrate, targetNode);
        }
    }

}
