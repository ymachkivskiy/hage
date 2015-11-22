package org.hage.platform.config.transl;

import com.google.common.collect.ImmutableMap;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.def.CellPopulationDescription;
import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.PopulationDistributionMap;
import org.hage.platform.config.loader.Configuration;
import org.hage.platform.habitat.structure.InternalPosition;
import org.hage.platform.habitat.structure.StructureDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;
import static org.hage.platform.config.def.PopulationDistributionMap.fromMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationComputationConfigurationTranslatorTest {

    @InjectMocks
    private ConfigurationComputationConfigurationTranslator tested;
    @Mock
    private PopulationDistributionMapCreator populationDistributionMapCreatorMock;

    @Test
    public void shouldTranslateToConfigurationWithSameGlobalComponents() throws Exception {

        // given

        IComponentDefinition firstComponentDefinition = mock(IComponentDefinition.class);
        IComponentDefinition secondComponentDefinition = mock(IComponentDefinition.class);

        final Configuration configuration = Configuration.builder()
                .habitatConfiguration(new HabitatExternalConfiguration(null, emptyList()))
                .globalComponents(asList(
                    firstComponentDefinition,
                    secondComponentDefinition
                )).build();

        // when

        ComputationConfiguration translatedConfiguration = tested.translate(configuration);

        // then

        assertThat(translatedConfiguration.getGlobalComponents()).containsOnly(firstComponentDefinition, secondComponentDefinition);


    }

    @Test
    public void shouldTranslateToConfigurationWithPopulationDistributionMergedFromAllPopulationDistributionQualifiers() throws Exception {

        // given

        final PopulationDistributionMap firstPopDistrMap = fromMap(
                ImmutableMap.of(
                        InternalPosition.definedBy(1, 2, 3), mock(CellPopulationDescription.class)
                )
        );

        final PopulationDistributionMap secondPopDistrMap = fromMap(
                ImmutableMap.of(
                        InternalPosition.definedBy(3, 2, 1), mock(CellPopulationDescription.class)
                )
        );

        final ChunkPopulationQualifier firstPopQ = new ChunkPopulationQualifier(null, null);
        final ChunkPopulationQualifier secondPopQ = new ChunkPopulationQualifier(null, null);

        final List<ChunkPopulationQualifier> chunkPopulationQualifiers = asList(
                firstPopQ,
                secondPopQ
        );

        when(populationDistributionMapCreatorMock.createMap(firstPopQ)).thenReturn(firstPopDistrMap);
        when(populationDistributionMapCreatorMock.createMap(secondPopQ)).thenReturn(secondPopDistrMap);


        final Configuration configuration = Configuration.builder().habitatConfiguration(new HabitatExternalConfiguration(mock(StructureDefinition.class), chunkPopulationQualifiers)).build();


        // when

        ComputationConfiguration translatedConf = tested.translate(configuration);

        // then

        assertThat(translatedConf.getHabitatConfiguration().getPopulationDistributionMap()).isEqualTo(firstPopDistrMap.merge(secondPopDistrMap));

    }

    @Test
    public void shouldTranslateToConfigurationWithSameStructureDefinition() throws Exception {

        // given

        StructureDefinition expectedStructureDefinition = mock(StructureDefinition.class);
        final HabitatExternalConfiguration externalConf = new HabitatExternalConfiguration(expectedStructureDefinition, emptyList());
        final Configuration configuration = Configuration.builder().habitatConfiguration(externalConf).build();

        // when

        ComputationConfiguration translatedConf = tested.translate(configuration);

        // then

        assertThat(translatedConf.getHabitatConfiguration().getStructureDefinition()).isSameAs(expectedStructureDefinition);

    }
}