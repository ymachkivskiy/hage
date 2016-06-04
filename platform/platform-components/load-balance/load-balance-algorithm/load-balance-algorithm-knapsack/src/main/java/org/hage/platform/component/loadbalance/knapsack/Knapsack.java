package org.hage.platform.component.loadbalance.knapsack;

import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
public class Knapsack {

    @Getter
    private long size = 0;

    private final Set<Item> items = new HashSet<>();

    public void addItem(Item item) {
        if (items.add(item)) {
            size += item.getSize();
        }
    }

}
