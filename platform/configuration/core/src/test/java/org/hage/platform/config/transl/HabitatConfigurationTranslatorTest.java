package org.hage.platform.config.transl;

import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.HabitatInternalConfiguration;
import org.hage.platform.habitat.structure.StructureDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HabitatConfigurationTranslatorTest {

    private HabitatConfigurationTranslator tested;

    @Before
    public void setUp() throws Exception {
        tested = new HabitatConfigurationTranslator();
    }

    @Test
    public void shouldContainSameStructureDefinition() throws Exception {

        // given

        final StructureDefinition expectedStructureDefinition = mock(StructureDefinition.class);
        final HabitatExternalConfiguration externalConfiguration = new HabitatExternalConfiguration(expectedStructureDefinition, emptyList());

        // when

        HabitatInternalConfiguration internalConfiguration = tested.toInternalModel(externalConfiguration);

        // then

        assertThat(internalConfiguration.getStructureDefinition()).isSameAs(expectedStructureDefinition);

    }

    @Test
    public void shouldCombinePopulationConfigurationFromAllPopulationQualifiers() throws Exception {

        Assert.fail("shouldCombinePopulationConfigurationFromAllPopulationQualifiers not implemented");
        // given

        // when

        // then


    }

}