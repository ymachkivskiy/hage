package org.hage.platform.config.gen.count;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.agent.AgentCountData.atMost;

public class AtMostCountProviderTest {

    private AtMostCountProvider tested;

    @Before
    public void setUp() throws Exception {
        tested = new AtMostCountProvider();
    }

    @Test
    public void shouldWriteSomeTests() throws Exception {

        // given

        final int maxValue = 339;

        // when

        // then

        for (int i = 0; i < 10000; i++) {
            assertThat(tested.getCountInternal(atMost(maxValue))).isLessThanOrEqualTo(maxValue);
        }

    }
}