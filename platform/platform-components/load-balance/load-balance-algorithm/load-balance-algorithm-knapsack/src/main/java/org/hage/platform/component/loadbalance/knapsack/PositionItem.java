package org.hage.platform.component.loadbalance.knapsack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.component.structure.Position;

@Getter
@RequiredArgsConstructor
public class PositionItem {
    private final Position position;
    private final int size;

    @Override
    public String toString() {
        return String.valueOf(size);
    }

}
