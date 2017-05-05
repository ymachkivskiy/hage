package org.hage.platform.cluster.loadbalance.knapsack.model;

import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.unmodifiableCollection;

@ToString(exclude = "id")
public class Knapsack {

    private static final AtomicLong counter = new AtomicLong(0);
    private final long id;

    @Getter
    private long size = 0;
    private final Set<Item> items = new HashSet<>();

    public Knapsack() {
        id = counter.getAndIncrement();
    }

    private Knapsack(Knapsack original) {
        size = original.size;
        items.addAll(original.items);
        id = original.id;
    }

    public void addItem(Item item) {
        if (items.add(item)) {
            size += item.getSize();
        }
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            size -= item.getSize();
        }
    }

    public Collection<Item> getItems() {
        return unmodifiableCollection(items);
    }

    public Knapsack copy() {
        return new Knapsack(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return id == ((Knapsack) o).id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
