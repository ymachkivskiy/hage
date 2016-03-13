package org.hage.util.proportion;

public interface ProportionsDivisor<T> {
    Division<T> divideUsingProportions(T source, Proportions proportions);
}
