package org.hage.util.proportion;

public interface ProportionsDivisor<T, PT extends Countable> {
    Division<T> divideUsingProportions(T source, Proportions<PT> proportions);
}
