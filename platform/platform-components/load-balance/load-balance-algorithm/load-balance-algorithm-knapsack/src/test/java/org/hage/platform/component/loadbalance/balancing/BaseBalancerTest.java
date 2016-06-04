package org.hage.platform.component.loadbalance.balancing;

import org.hage.platform.component.loadbalance.input.KnapsackAllocation;
import org.hage.platform.component.loadbalance.knapsack.Knapsack;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ContextConfiguration(locations = "classpath:load-balance-algorithm-knapsack.test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseBalancerTest {

    @Autowired
    private Balancer balancer;

    @Test
    public void shouldNotProduceTransfersForEmptyInput() {
        // given
        BalancingInput balancingInput = new BalancingInput(emptyList());

        // when
        Collection<KnapsackTransfer> transfers = balancer.balance(balancingInput);

        // then
        assertThat(transfers).isEmpty();
    }

    @Test
    public void shouldNotProduceTransfersForBalancedInput() {
        // given
        BalancingInput balancingInput = new BalancingInput(
            asList(
                new KnapsackAllocation(10, knapsackOfSize(10)),
                new KnapsackAllocation(1, knapsackOfSize(1)),
                new KnapsackAllocation(153, knapsackOfSize(153)),
                new KnapsackAllocation(16, knapsackOfSize(16)),
                new KnapsackAllocation(1024, knapsackOfSize(1024))
            )
        );

        // when
        Collection<KnapsackTransfer> transfers = balancer.balance(balancingInput);

        // then
        assertThat(transfers).isEmpty();
    }

    @Test
    public void shouldNotProduceTransfersForOptimallyBalancedInput() {
        // given
        Assert.fail("NOT IMPLEMENTED");
        new BalancingInput(
            asList(
                new KnapsackAllocation(7, knapsackOfSize(8)),
                new KnapsackAllocation(3, knapsackOfSize(2))

            )
        );

        // when


        // then

    }

    @Test
    public void shouldPerformSimpleIdealBalancing() {
        // given
        Assert.fail("NOT IMPLEMENTED");

        // when


        // then

    }


    @Test
    public void shouldOptimizeBalancing() {
        // given
        Assert.fail("NOT IMPLEMENTED");

        // when


        // then

    }

    private static Knapsack knapsackOfSize(long size) {
        Knapsack knapsack = mock(Knapsack.class);
        given(knapsack.getSize()).willReturn(size);
        return knapsack;
    }

}