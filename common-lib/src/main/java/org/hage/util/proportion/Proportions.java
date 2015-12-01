package org.hage.util.proportion;

import com.google.common.primitives.UnsignedInteger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toMap;

public class Proportions {
    private final Map<Countable, BigDecimal> fractionsMap;

    private Proportions(Map<Countable, BigDecimal> fractionsMap) {
        this.fractionsMap = unmodifiableMap(fractionsMap);
    }

    public Set<Countable> getElements() {
        return unmodifiableSet(fractionsMap.keySet());
    }

    public BigDecimal getFractionFor(Countable countable) {
        return fractionsMap.getOrDefault(countable, BigDecimal.ZERO);
    }

    public static Proportions forCountable(Collection<Countable> countable) {
        BigInteger summary = countable
            .stream()
            .map(Countable::getCount)
            .map(UnsignedInteger::bigIntegerValue)
            .reduce(BigInteger.ZERO, BigInteger::add);

        BigDecimal sum = new BigDecimal(summary);

        Map<Countable, BigDecimal> fractionMap = countable
            .stream()
            .filter(c -> c.getCount().compareTo(UnsignedInteger.ZERO) != 0)
            .collect(toMap(
                (x) -> x,
                c -> {
                    BigDecimal bd = new BigDecimal(c.getCount().bigIntegerValue());
                    return bd.divide(sum, 32, BigDecimal.ROUND_CEILING);
                }
            ));

        return new Proportions(fractionMap);
    }

    public static Proportions forCountable(Countable... countable) {
        return forCountable(asList(countable));
    }

}
