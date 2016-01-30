package org.hage.platform.config.gen.count;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.fixed;

public class FixedCountProviderTest {

    private FixedCountProvider tested;

    @Before
    public void setUp() throws Exception {
        tested = new FixedCountProvider();
    }

    @Test
    public void shouldWriteSomeTest() throws Exception {

        // given

        final int fixedCount = 1124;

        // when

        // then

        for (int i = 0; i < 10000; i++) {
            assertThat(tested.getAgentCount(fixed(fixedCount))).isEqualTo(fixedCount);
        }

    }
}