package org.hage.platform.component.loadbalance.core.remote.endpoint;

public enum LoadBalanceMessageType {
    PAUSE_NODE,
    REQUEST_FOR_MONITORING_DATA,
    MONITORING_DATA_RESPONSE,
    REORGANIZATION_ORDER,
    START_NODE,
    ACK
}
