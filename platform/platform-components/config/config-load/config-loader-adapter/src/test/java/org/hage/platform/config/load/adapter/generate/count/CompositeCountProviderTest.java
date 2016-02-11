package org.hage.platform.config.load.adapter.generate.count;

import org.hage.platform.config.load.definition.agent.AgentCountData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.config.load.definition.agent.AgentCountData.*;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CompositeCountProviderTest {

    private CompositeCountProvider tested;

    @Mock
    private AgentCountProvider fixedCountProvider;
    @Mock
    private AgentCountProvider atLeastCountProvider;
    @Mock
    private AgentCountProvider atMostCountProvider;
    @Mock
    private AgentCountProvider betweenCountProvider;
    @Mock
    private AgentCountProvider randomCountProvider;

    @Before
    public void setUp() throws Exception {
        tested = new CompositeCountProvider();

        tested.setAtLeastCountProvider(atLeastCountProvider);
        tested.setAtMostCountProvider(atMostCountProvider);
        tested.setBetweenCountProvider(betweenCountProvider);
        tested.setFixedCountProvider(fixedCountProvider);
        tested.setRandomCountProvider(randomCountProvider);

    }

    @Test
    public void shouldUseFixedCountProvider() throws Exception {

        // given

        final AgentCountData countData = fixed(10);

        // when

        tested.getAgentCount(countData);

        // then

        verify(fixedCountProvider).getAgentCount(same(countData));

    }

    @Test
    public void shouldUseAtLeasCountProvider() throws Exception {

        // given

        final AgentCountData countData = atLeast(10);

        // when

        tested.getAgentCount(countData);

        // then

        verify(atLeastCountProvider).getAgentCount(same(countData));


    }


    @Test
    public void shouldUseAtMostCountProvider() throws Exception {

        // given

        final AgentCountData countData = atMost(10);

        // when

        tested.getAgentCount(countData);

        // then

        verify(atMostCountProvider).getAgentCount(same(countData));

    }

    @Test
    public void shouldUseBetweenCountProvider() throws Exception {

        // given

        final AgentCountData countData = between(10, 14);

        // when

        tested.getAgentCount(countData);

        // then

        verify(betweenCountProvider).getAgentCount(same(countData));

    }

    @Test
    public void shouldUseRandomCountProvider() throws Exception {

        // given

        final AgentCountData countData = random();

        // when

        tested.getAgentCount(countData);

        // then

        verify(randomCountProvider).getAgentCount(same(countData));

    }
}