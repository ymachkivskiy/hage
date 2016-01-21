package org.hage.platform.config.local.gen.count;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.between;

public class BetweenAgentCountProviderTest {

    private BetweenAgentCountProvider tested;

    @Before
    public void setUp() throws Exception {
        tested = new BetweenAgentCountProvider();
    }

    @Test
    public void shouldWriteSomeTests() throws Exception {

        // given

        final int minValue = 157;
        final int maxValue = 213;

        // when

        // then

        for (int i = 0; i < 10000; i++) {
            int generatedCount = tested.getAgentCount(between(minValue, maxValue));
            assertThat(generatedCount).isGreaterThanOrEqualTo(minValue);
            assertThat(generatedCount).isLessThanOrEqualTo(maxValue);
        }

    }
}