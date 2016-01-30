package org.hage.platform.config.gen.select;

import org.hage.platform.config.def.agent.PositionsSelectionData;
import org.hage.platform.habitat.structure.Chunk;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.config.def.agent.PositionsSelectionData.allPositions;
import static org.hage.platform.config.def.agent.PositionsSelectionData.randomPositions;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CompositePositionsSelectorTest {

    private CompositePositionsSelector tested;

    @Mock
    private PositionsSelector allSelector;
    @Mock
    private PositionsSelector randomPositionRandomNumberSelector;
    @Mock
    private PositionsSelector randomPositionsFixedNumberSelector;

    private Chunk chunk = new Chunk(null, null);

    @Before
    public void setUp() throws Exception {
        tested = new CompositePositionsSelector();


        tested.setAllSelector(allSelector);
        tested.setRandomPositionsFixedNumberSelector(randomPositionsFixedNumberSelector);
        tested.setRandomPositionsRandomNumberSelector(randomPositionRandomNumberSelector);
    }

    @Test
    public void shouldUseAllSelector() throws Exception {

        // given

        final PositionsSelectionData selectionData = allPositions();

        // when

        tested.select(chunk, selectionData);

        // then


        verify(allSelector).select(same(chunk), same(selectionData));

    }

    @Test
    public void shouldUseRandomFixedSelector() throws Exception {

        // given

        final PositionsSelectionData selectionData = randomPositions(21);

        // when

        tested.select(chunk, selectionData);

        // then


        verify(randomPositionsFixedNumberSelector).select(same(chunk), same(selectionData));

    }

    @Test
    public void shouldUseRandomRandomSelector() throws Exception {

        // given

        final PositionsSelectionData selectionData = randomPositions();

        // when

        tested.select(chunk, selectionData);

        // then


        verify(randomPositionRandomNumberSelector).select(same(chunk), same(selectionData));
    }
}