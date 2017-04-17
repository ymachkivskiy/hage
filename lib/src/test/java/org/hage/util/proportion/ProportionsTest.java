package org.hage.util.proportion;

import com.google.common.primitives.UnsignedInteger;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static org.fest.assertions.Assertions.assertThat;
import static org.hage.util.proportion.Proportions.forCountable;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProportionsTest {

    @Test
    public void shouldCreateProportionsWithoutElements() throws Exception {

        // given

        // when

        Proportions tested = forCountable(emptyList());

        // then

        assertThat(tested.getElements()).isEmpty();

    }

    @Test
    public void shouldContainAllOriginalElements() throws Exception {

        // given

        final Countable firstCountable = createCountable();
        final Countable secondCountable = createCountable();
        final Countable thirdCountable = createCountable();

        // when

        Proportions tested = forCountable(asList(firstCountable, secondCountable, thirdCountable));

        // then

        assertThat(tested.getElements()).containsOnly(firstCountable, secondCountable, thirdCountable);


    }

    @Test
    public void shouldReturnOneForProportionsWithOneElement() throws Exception {

        // given

        final Countable countable = createCountable();

        // when

        Proportions tested = forCountable(singletonList(countable));

        // then

        assertThat(tested.getFractionFor(countable)).isEqualByComparingTo(BigDecimal.ONE);

    }

    @Test
    public void shouldReturnZeroFractionForCountableNotFromProportions() throws Exception {

        // given

        // when

        Proportions tested = forCountable(singletonList(createCountable()));

        // then

        assertThat(tested.getFractionFor(mock(Countable.class))).isEqualTo(BigDecimal.ZERO);

    }

    @Test
    public void shouldReturnEqualFractionForEqualCountable() throws Exception {

        // given

        final UnsignedInteger number = UnsignedInteger.valueOf(1347);

        final Countable firstCountable = createCountableWithNumber(number);
        final Countable secondCountable = createCountableWithNumber(number);
        final Countable thirdCountable = createCountableWithNumber(number);
        // when

        Proportions tested = forCountable(asList(firstCountable, secondCountable, thirdCountable));

        // then

        assertThat(tested.getFractionFor(firstCountable)).isEqualTo(tested.getFractionFor(secondCountable));
        assertThat(tested.getFractionFor(secondCountable)).isEqualTo(tested.getFractionFor(thirdCountable));

    }

    @Test
    public void shouldReturnProportionalFractions() throws Exception {

        // given

        final Countable firstCountable = createCountableWithNumber(UnsignedInteger.valueOf(7));
        final Countable secondCountable = createCountableWithNumber(UnsignedInteger.valueOf(5));
        final Countable thirdCountable = createCountableWithNumber(UnsignedInteger.valueOf(13));

        // when

        Proportions tested = forCountable(asList(firstCountable, secondCountable, thirdCountable));

        // then

        assertThat(tested.getFractionFor(firstCountable)).isEqualByComparingTo(BigDecimal.valueOf(0.28));
        assertThat(tested.getFractionFor(secondCountable)).isEqualByComparingTo(BigDecimal.valueOf(0.2));
        assertThat(tested.getFractionFor(thirdCountable)).isEqualByComparingTo(BigDecimal.valueOf(0.52));


    }

    @Test
    public void shouldReturnFractionsFromZeroToOne() throws Exception {

        // given

        Random rand = new Random();

        List<Countable> countableList = rangeClosed(1, 1000)
            .mapToObj(i -> createCountableWithNumber(UnsignedInteger.valueOf(rand.nextInt(10000) + 1)))
            .collect(toList());

        // when

        Proportions<Countable> tested = forCountable(countableList);

        // then

        for (Countable countable : tested.getElements()) {
            BigDecimal fraction = tested.getFractionFor(countable);
            assertThat(fraction).isPositive().isNotZero().isLessThanOrEqualTo(BigDecimal.ONE);
        }

    }

    @Test
    public void shouldNotContainCountableWithZeroCount() throws Exception {

        // given

        final Countable zeroCountable = createCountableWithNumber(UnsignedInteger.ZERO);

        // when

        Proportions tested = forCountable(asList(zeroCountable, createCountableWithNumber(UnsignedInteger.valueOf(23))));

        // then

        assertThat(tested.getElements()).excludes(zeroCountable);

    }

    @Test
    public void shouldSumFractionsToOne() throws Exception {

        // given

        Random rand = new Random();

        List<Countable> countableList = rangeClosed(1, 1000)
            .mapToObj(i -> createCountableWithNumber(UnsignedInteger.valueOf(rand.nextInt(1024221) + 1)))
            .collect(toList());

        // when

        Proportions<Countable> tested = forCountable(countableList);

        // then

        BigDecimal sum = tested.getElements()
            .stream()
            .map(tested::getFractionFor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        MatcherAssert.assertThat(sum, is(closeTo(BigDecimal.ONE, new BigDecimal("0.0000000001"))));

    }

    private static Countable createCountableWithNumber(UnsignedInteger number) {
        final Countable firstCountable = mock(Countable.class);
        when(firstCountable.getNormalizedCapacity()).thenReturn(number);
        return firstCountable;
    }

    private static Countable createCountable() {
        return createCountableWithNumber(UnsignedInteger.valueOf(new Random().nextInt(253) + 1));
    }
}