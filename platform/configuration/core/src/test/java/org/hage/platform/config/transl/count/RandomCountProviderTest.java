package org.hage.platform.config.transl.count;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.random;

public class RandomCountProviderTest {

    private RandomCountProvider tested;

    @Before
    public void setUp() throws Exception {
        tested = new RandomCountProvider();
    }

    @Test
    public void shouldWriteSomeTest() throws Exception {

        // given

        // when

        // then

        for (int i = 0; i < 50000; i++) {
            assertThat(tested.getAgentCount(random())).isGreaterThanOrEqualTo(0);
        }

    }
}