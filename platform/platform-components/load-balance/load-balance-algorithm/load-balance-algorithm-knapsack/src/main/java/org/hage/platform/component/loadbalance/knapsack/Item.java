package org.hage.platform.component.loadbalance.knapsack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Item {
    @Getter
    private final int size;
}
