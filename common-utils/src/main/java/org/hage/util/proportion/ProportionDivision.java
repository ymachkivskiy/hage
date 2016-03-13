package org.hage.util.proportion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ProportionDivision<T> {
    private final Map<Countable, T> divisionMap;
    private final List<DivisionPart<T>> divisions;

    public ProportionDivision(List<DivisionPart<T>> divisionParts) {
        this.divisions = divisionParts;
        this.divisionMap = divisionParts.stream()
            .collect(toMap(
                DivisionPart::getCountable,
                DivisionPart::getPart
            ));
    }

    public T getFor(Countable countable) {
        checkArgument(divisionMap.containsKey(countable));
        return divisionMap.get(countable);
    }

    public List<DivisionPart<T>> getDivisions() {
        return new ArrayList<>(divisions);
    }

    public <D> ProportionDivision<D> translateTo(Function<T, D> translator) {
        return new ProportionDivision<>(
            divisions.stream().map(
                dp -> new DivisionPart<>(dp.getCountable(), translator.apply(dp.getPart()))
            ).collect(toList())
        );
    }
}
