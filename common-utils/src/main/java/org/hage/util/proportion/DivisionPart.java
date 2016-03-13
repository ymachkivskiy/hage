package org.hage.util.proportion;

import lombok.Data;

@Data
public class DivisionPart<T> {
    private final Countable countable;
    private final T part;
}
