package org.hage.util.proportion;

import com.google.common.primitives.UnsignedInteger;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@ToString
public class Proportions<T extends Countable> {
    private final Map<T, BigDecimal> fractionsMap;

    private Proportions(Map<T, BigDecimal> fractionsMap) {
        this.fractionsMap = unmodifiableMap(fractionsMap);
    }

    public Set<T> getElements() {
        return unmodifiableSet(fractionsMap.keySet());
    }

    public BigDecimal getFractionFor(T countable) {
        return fractionsMap.getOrDefault(countable, BigDecimal.ZERO);
    }

    public List<CountableFraction<T>> getFractions() {
        return fractionsMap.entrySet().stream()
            .map(e -> new CountableFraction<>(e.getKey(), e.getValue()))
            .collect(toList());
    }

    public static <T extends Countable> Proportions<T> forCountable(Collection<? extends T> countable) {
        BigInteger summary = countable
            .stream()
            .map(Countable::getNormalizedCapacity)
            .map(UnsignedInteger::bigIntegerValue)
            .reduce(BigInteger.ZERO, BigInteger::add);

        BigDecimal sum = new BigDecimal(summary);

        Map<T, BigDecimal> fractionMap = countable
            .stream()
            .filter(c -> c.getNormalizedCapacity().compareTo(UnsignedInteger.ZERO) != 0)
            .collect(toMap(
                identity(),
                c -> {
                    BigDecimal bd = new BigDecimal(c.getNormalizedCapacity().bigIntegerValue());
                    return bd.divide(sum, 32, BigDecimal.ROUND_CEILING);
                }
            ));

        return new Proportions<>(fractionMap);
    }

    @SafeVarargs
    public static <T extends Countable> Proportions<T> forCountable(T... countable) {
        return forCountable(asList(countable));
    }

    @Data
    public static class CountableFraction<T extends Countable> {
        private final T countable;
        private final BigDecimal fraction;
    }

}
