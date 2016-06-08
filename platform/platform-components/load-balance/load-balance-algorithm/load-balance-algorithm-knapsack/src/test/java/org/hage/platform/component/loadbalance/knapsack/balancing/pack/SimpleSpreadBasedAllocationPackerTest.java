package org.hage.platform.component.loadbalance.knapsack.balancing.pack;

import org.hage.platform.component.loadbalance.knapsack.balancing.KnapsackAllocation;
import org.hage.platform.component.loadbalance.knapsack.model.Item;
import org.hage.platform.component.loadbalance.knapsack.model.Knapsack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.component.loadbalance.knapsack.util.AllocationUtils.calculateAllocationsSpread;

@ContextConfiguration(locations = "classpath:load-balance-algorithm-knapsack.test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleSpreadBasedAllocationPackerTest {

    @Autowired
    private AllocationsPacker packer;

    @Test
    public void shouldReturnEmptyAllocationsForEmptyInput() {
        // given
        Collection<KnapsackAllocation> knapsackAllocations = emptyList();

        // when
        Collection<KnapsackAllocation> repacked = packer.repack(knapsackAllocations);

        // then
        assertThat(repacked).isEmpty();
    }

    @Test
    public void shouldNotRepackWithWorseSpread() {
        // given
        List<KnapsackAllocation> inputAllocations = asList(
            new KnapsackAllocation(31, knapsackWithItems(13, 3, 5)),
            new KnapsackAllocation(17, knapsackWithItems(11, 21)),
            new KnapsackAllocation(34, knapsackWithItems(4, 7, 18))
        );
        // when
        Collection<KnapsackAllocation> repacked = packer.repack(inputAllocations);

        // then
        assertThat(calculateAllocationsSpread(repacked))
            .isLessThanOrEqualTo(calculateAllocationsSpread(inputAllocations));
    }


    @Test
    public void shouldRepackWithZeroSpreadForBalancedInput() {
        // given
        List<KnapsackAllocation> knapsackAllocations = asList(
            new KnapsackAllocation(10, knapsackWithItems(10)),
            new KnapsackAllocation(1, knapsackWithItems(1)),
            new KnapsackAllocation(153, knapsackWithItems(153)),
            new KnapsackAllocation(16, knapsackWithItems(16)),
            new KnapsackAllocation(1024, knapsackWithItems(1024))
        );

        // when
        Collection<KnapsackAllocation> repacked = packer.repack(knapsackAllocations);

        // then
        assertThat(calculateAllocationsSpread(repacked)).isZero();
    }


    @Test
    public void shouldRepackIdeallyBalancingAllocationsToZeroSpreadAllocations() {
        // given
        List<KnapsackAllocation> inputAllocations = asList(
            new KnapsackAllocation(25, knapsackWithItems(1, 9)),
            new KnapsackAllocation(54, knapsackWithItems(23, 31, 15)),
            new KnapsackAllocation(13, knapsackWithItems(2)),
            new KnapsackAllocation(24, knapsackWithItems(11, 7, 17))
        );

        // when
        Collection<KnapsackAllocation> repacked = packer.repack(inputAllocations);

        // then
        assertThat(calculateAllocationsSpread(repacked)).isZero();
    }


    @Test
    public void shouldPerformBalancingWhichOptimizeInputBalance() {
        // given
        List<KnapsackAllocation> inputAllocations = asList(
            new KnapsackAllocation(5, knapsackWithItems(6)),
            new KnapsackAllocation(17, knapsackWithItems(14, 7)),
            new KnapsackAllocation(10, knapsackWithItems(2, 3))
        );

        // when
        Collection<KnapsackAllocation> repacked = packer.repack(inputAllocations);

        // then
        assertThat(calculateAllocationsSpread(repacked))
            .isLessThan(calculateAllocationsSpread(inputAllocations))
            .isGreaterThanOrEqualTo(6);
    }

    private static Knapsack knapsackWithItems(int... itemSizes) {
        Knapsack knapsack = new Knapsack();

        stream(itemSizes)
            .mapToObj(Item::new)
            .forEach(knapsack::addItem);

        return knapsack;
    }

}