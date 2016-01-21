package org.hage.platform.config.local.gen.count;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.atLeast;

public class AtLeastCountProviderTest {

    private AtLeastCountProvider tested;

    @Before
    public void setUp() throws Exception {
        tested = new AtLeastCountProvider();
    }

    @Test
    public void shouldGenerateNumbersAtLeast() throws Exception {

        // given

        final int minValue = 1197;

        // when

        // then

        for (int i = 0; i < 10000; i++) {
            assertThat(tested.getAgentCount(atLeast(minValue))).isGreaterThanOrEqualTo(minValue);
        }

    }
}