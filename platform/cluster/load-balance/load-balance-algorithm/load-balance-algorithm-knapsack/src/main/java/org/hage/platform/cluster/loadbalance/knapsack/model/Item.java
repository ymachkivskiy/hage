package org.hage.platform.cluster.loadbalance.knapsack.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Item {
    @Getter
    private final int size;
}
