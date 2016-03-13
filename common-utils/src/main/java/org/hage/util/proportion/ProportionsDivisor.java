package org.hage.util.proportion;

public interface ProportionsDivisor<T> {
    ProportionDivision<T> divideUsingProportions(T source, Proportions proportions);
}
