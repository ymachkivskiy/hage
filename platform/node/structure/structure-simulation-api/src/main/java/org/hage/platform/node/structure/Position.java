package org.hage.platform.node.structure;

import lombok.EqualsAndHashCode;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
@EqualsAndHashCode(doNotUseGetters = true)
public final class Position implements Serializable {
    public static final Position ZERO = position(0, 0, 0);

    public final int depth;
    public final int horizontal;
    public final int vertical;

    private Position(int depth, int horizontal, int vertical) {
        this.depth = depth;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Position withDepth(int depth) {
        return position(depth, this.horizontal, this.vertical);
    }

    public Position withHorizontal(int horizontal) {
        return position(this.depth, horizontal, this.vertical);
    }

    public Position withVertical(int vertical) {
        return position(this.depth, this.horizontal, vertical);
    }

    public Position shift(int depthShift, int horizontalShift, int verticalShift) {
        return position(depth + depthShift, horizontal + horizontalShift, vertical + verticalShift);
    }

    public static Position position(int depth, int horizontal, int vertical) {
        return new Position(depth, horizontal, vertical);
    }

    @Override
    public String toString() {
        return "pos(d=" + depth + ",h=" + horizontal + ",v=" + vertical + ")";
    }
}
