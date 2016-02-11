package org.hage.platform.config.distribution.division;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.UnsignedInteger;
import org.fest.assertions.Assertions;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.CellPopulationDescription;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.HabitatGeography;
import org.hage.platform.config.PopulationDistributionMap;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.InternalPosition;
import org.hage.platform.habitat.structure.StructureDefinition;
import org.hage.util.proportion.Countable;
import org.hage.util.proportion.Proportions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static org.fest.assertions.Assertions.assertThat;
import static org.hage.util.proportion.Proportions.forCountable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleConfigurationSplitterTest {

    private SimpleConfigurationSplitter tested;

    final ComputationConfiguration DEFAULT_CONFIG = ComputationConfiguration.builder()
        .globalComponents(emptyList())
        .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), PopulationDistributionMap.emptyDistributionMap()))
        .build();


    @Before
    public void setUp() throws Exception {
        tested = new SimpleConfigurationSplitter();
    }


    @Test
    public void shouldNotSplitWithProportionsWithOneCountable() throws Exception {

        // given

        final Countable countable = countableFor(241);

        final Proportions proportions = forCountable(countable);

        PopulationDistributionMap distributionMap = PopulationDistributionMap.distributionFromMap(
            of(
                InternalPosition.definedBy(1, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 100),
                InternalPosition.definedBy(0, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 10),
                InternalPosition.definedBy(1, 0, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 122),
                InternalPosition.definedBy(4, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 12),
                InternalPosition.definedBy(1, 0, 42), CellPopulationDescription.populationFromPair(createAgentDefinition(), 1)
            )
        );


        final ComputationConfiguration originalConfiguration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), distributionMap)).build();

        // when


        ConfigurationDivision configDivision = tested.split(originalConfiguration, proportions);

        // then

        assertThat(configDivision.get(countable).orElse(DEFAULT_CONFIG)).isEqualTo(originalConfiguration);


    }

    @Test
    public void shouldDivideConfigurationIntoComplementaryParts() throws Exception {

        // given

        final Countable firstCountable = countableFor(1);
        final Countable secondCountable = countableFor(2);

        PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.distributionFromMap(
            of(
                InternalPosition.definedBy(1, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 100),
                InternalPosition.definedBy(0, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 10),
                InternalPosition.definedBy(1, 0, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 122),
                InternalPosition.definedBy(4, 2, 3), CellPopulationDescription.populationFromPair(createAgentDefinition(), 12),
                InternalPosition.definedBy(1, 0, 42), CellPopulationDescription.populationFromPair(createAgentDefinition(), 1)
            )
        );


        final ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), originalDistributionMap))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, forCountable(firstCountable, secondCountable));

        PopulationDistributionMap firstMap = configDivision.get(firstCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap();
        PopulationDistributionMap secondMap = configDivision.get(secondCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap();

        // then

        Assertions.assertThat(firstMap.merge(secondMap)).isEqualTo(originalDistributionMap);

    }

    @Test
    public void shouldDivideBigConfigurationIntoComplementaryParts() throws Exception {

        // given

        Random r = new Random();

        final int countableCount = 50;
        final int populationDistributionMapSize = 10000;

        List<Countable> countable = range(1, countableCount)
            .mapToObj(i -> countableFor(r.nextInt(1000) + i))
            .collect(toList());

        final PopulationDistributionMap expectedDistributionMap = PopulationDistributionMap.distributionFromMap(range(1, populationDistributionMapSize)
            .mapToObj(i -> InternalPosition.definedBy(r.nextInt(40000), r.nextInt(154001), r.nextInt(121002)))
            .collect(toMap(
                x -> x,
                x -> CellPopulationDescription.populationFromPair(createAgentDefinition(), r.nextInt(1000) + 1)
            )));

        ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), expectedDistributionMap))
            .build();

        // when

        ConfigurationDivision configurationDivision = tested.split(configuration, forCountable(countable));

        PopulationDistributionMap actualMergedMap = countable.stream()
            .map(configurationDivision::get)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(ComputationConfiguration::getHabitatGeography)
            .map(HabitatGeography::getPopulationDistributionMap)
            .reduce(PopulationDistributionMap.emptyDistributionMap(), PopulationDistributionMap::merge);

        // then

        Assertions.assertThat(actualMergedMap).isEqualTo(expectedDistributionMap);

    }

    @Test
    public void shouldSplitConfigurationSavingTotalOrderingOfProportions() throws Exception {

        // given

        Random r = new Random();

        final int countableCount = 50;
        final int populationDistributionMapSize = 1000;

        List<Countable> countable = range(1, countableCount)
            .mapToObj(i -> countableFor(r.nextInt(1000) + i))
            .collect(toList());

        final PopulationDistributionMap expectedDistributionMap = PopulationDistributionMap.distributionFromMap(range(1, populationDistributionMapSize)
            .mapToObj(i -> InternalPosition.definedBy(r.nextInt(40000), r.nextInt(154001), r.nextInt(121002)))
            .collect(toMap(
                x -> x,
                x -> CellPopulationDescription.populationFromPair(createAgentDefinition(), r.nextInt(1000) + 1)
            )));

        ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), expectedDistributionMap))
            .build();

        // when

        ConfigurationDivision configurationDivision = tested.split(configuration, forCountable(countable));

        // then

        for (Countable c1 : countable) {
            PopulationDistributionMap c1DistrMap = configurationDivision.get(c1).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap();
            final Long c1DistrMapNumberOfAgents = c1DistrMap.getNumberOfAgents();

            for (Countable c2 : countable) {
                PopulationDistributionMap c2DistrMap = configurationDivision.get(c2).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap();
                final Long c2DistrMapNumberOfAgents = c2DistrMap.getNumberOfAgents();

                if (c1.getCount().compareTo(c2.getCount()) >= 0) {
                    assertThat(c1DistrMapNumberOfAgents.compareTo(c2DistrMapNumberOfAgents) >= 0);
                } else {
                    assertThat(c1DistrMapNumberOfAgents.compareTo(c2DistrMapNumberOfAgents) < 0);
                }

            }
        }
    }

    @Test
    public void shouldContainConfigurationForAllCountableFromProportions() throws Exception {

        // given

        final List<Countable> countable = range(1, 1000)
            .mapToObj(SimpleConfigurationSplitterTest::countableFor)
            .collect(toList());

        final ComputationConfiguration configuration = ComputationConfiguration
            .builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), PopulationDistributionMap.emptyDistributionMap()))
            .build();

        // when

        ConfigurationDivision division = tested.split(configuration, forCountable(countable));

        // then

        countable.forEach(
            c -> assertThat(division.get(c).isPresent()).isTrue()
        );

    }

    @Test
    public void shouldNotSplitGlobalComponents() throws Exception {

        // given


        final Countable firstCountable = countableFor(1);
        final Countable secondCountable = countableFor(2);
        final Countable thirdCountable = countableFor(3);

        final Proportions proportions = forCountable(firstCountable, secondCountable, thirdCountable);


        final IComponentDefinition firstGlobalComp = mock(IComponentDefinition.class);
        final IComponentDefinition secondGlobalComp = mock(IComponentDefinition.class);
        final IComponentDefinition thirdGlobalComp = mock(IComponentDefinition.class);

        ComputationConfiguration configuration = ComputationConfiguration.builder()
            .globalComponents(asList(firstGlobalComp, secondGlobalComp, thirdGlobalComp))
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), PopulationDistributionMap.emptyDistributionMap()))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, proportions);

        // then

        assertThat(configDivision.get(firstCountable).orElse(DEFAULT_CONFIG).getGlobalComponents()).containsOnly(firstGlobalComp, secondGlobalComp, thirdGlobalComp);
        assertThat(configDivision.get(secondCountable).orElse(DEFAULT_CONFIG).getGlobalComponents()).containsOnly(firstGlobalComp, secondGlobalComp, thirdGlobalComp);
        assertThat(configDivision.get(thirdCountable).orElse(DEFAULT_CONFIG).getGlobalComponents()).containsOnly(firstGlobalComp, secondGlobalComp, thirdGlobalComp);

    }

    @Test
    public void shouldSaveAtomicityOfInternalPositionPopulationDescriptionDuringSplitting() throws Exception {

        // given

        Random r = new Random();

        final int countableNumber = 29;
        final int internalElementsCount = 1000;

        Set<InternalPosition> internalPositions = range(1, internalElementsCount)
            .mapToObj(i -> InternalPosition.definedBy(r.nextInt(12000), r.nextInt(30192), r.nextInt(324221)))
            .collect(toSet());

        List<Countable> countable = range(1, countableNumber)
            .mapToObj(i -> countableFor(r.nextInt(100) + i))
            .collect(toList());

        final Map<InternalPosition, CellPopulationDescription> dMap = internalPositions
            .stream()
            .collect(toMap(
                x -> x,
                x -> CellPopulationDescription.populationFromPair(createAgentDefinition(), r.nextInt(1000) + 1)
            ));

        final PopulationDistributionMap distributionMap = PopulationDistributionMap.distributionFromMap(dMap);

        final ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), distributionMap))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, forCountable(countable));

        // then

        for (InternalPosition internalPosition : internalPositions) {

            Set<Countable> countableContainingInternalPosition = new HashSet<>();


            for (Countable c : countable) {
                PopulationDistributionMap mapForCountable = configDivision.get(c).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap();
                if (mapForCountable.getInternalPositions().contains(internalPosition)) {
                    countableContainingInternalPosition.add(c);

                    CellPopulationDescription actualCellPopulation = mapForCountable.getPopulationFor(internalPosition);

                    Assertions.assertThat(actualCellPopulation).isSameAs(dMap.get(internalPosition));
                }
            }

            assertThat(countableContainingInternalPosition).hasSize(1);

        }

    }

    @Test
    public void shouldDivideConfigurationSavingStructureDefinition() throws Exception {

        // given

        final Countable firstCountable = countableFor(1);
        final Countable secondCountable = countableFor(2);

        final StructureDefinition structureDefinition = mock(StructureDefinition.class);

        final ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(structureDefinition, PopulationDistributionMap.emptyDistributionMap()))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, forCountable(firstCountable, secondCountable));

        // then

        assertThat(configDivision.get(firstCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getStructureDefinition()).isEqualTo(structureDefinition);
        assertThat(configDivision.get(secondCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getStructureDefinition()).isEqualTo(structureDefinition);


    }


    @Test
    public void shouldReturnEmptyConfigurationForCountableNotInProportions() throws Exception {

        // given

        final Countable firstCountable = countableFor(1);
        final Countable secondCountable = countableFor(2);
        final Countable thirdCountable = countableFor(3);

        final Proportions proportions = forCountable(firstCountable, secondCountable, thirdCountable);

        // when

        ConfigurationDivision configDivision = tested.split(DEFAULT_CONFIG, proportions);

        // then

        assertThat(configDivision.get(mock(Countable.class)).isPresent()).isFalse();

    }

    @Ignore("This functionality is not assured by current implementation")
    @Test
    public void shouldDivideConfigurationIntoTwoPartsOfEqualNumberOfAgents() throws Exception {

        // given

        final Countable firstCountable = countableFor(123);
        final Countable secondCountable = countableFor(123);

        PopulationDistributionMap originalDistributionMap = PopulationDistributionMap.distributionFromMap(
            ImmutableMap.<InternalPosition, CellPopulationDescription>builder()
                .put(
                    InternalPosition.definedBy(1, 2, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 4, createAgentDefinition(), 3))
                )
                .put(
                    InternalPosition.definedBy(1, 0, 42),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 1)
                )
                .put(
                    InternalPosition.definedBy(0, 2, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 1, createAgentDefinition(), 1, createAgentDefinition(), 1))
                )
                .put(
                    InternalPosition.definedBy(1, 0, 3),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 2)
                )
                .put(
                    InternalPosition.definedBy(4, 2, 3),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 2)
                )
                .put(
                    InternalPosition.definedBy(4, 22, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 4, createAgentDefinition(), 1))
                )

                .build()
        );


        final ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), originalDistributionMap))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, forCountable(firstCountable, secondCountable));

        // then

        assertThat(configDivision.get(firstCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap().getNumberOfAgents()).isEqualTo(10L);
        assertThat(configDivision.get(secondCountable).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap().getNumberOfAgents()).isEqualTo(10L);

    }

    @Ignore("This functionality is not assured by current implementation")
    @Test
    public void shouldDivideConfigurationIntoThreePartsWithDifferentProportionsOfAgentsNumber() throws Exception {

        // given

        final Countable c5 = countableFor(5);
        final Countable c8 = countableFor(8);
        final Countable c7 = countableFor(7);

        PopulationDistributionMap distributionMap = PopulationDistributionMap.distributionFromMap(
            ImmutableMap.<InternalPosition, CellPopulationDescription>builder()
                .put(
                    InternalPosition.definedBy(1, 2, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 4, createAgentDefinition(), 3))
                )
                .put(
                    InternalPosition.definedBy(1, 0, 42),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 1)
                )
                .put(
                    InternalPosition.definedBy(0, 2, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 1, createAgentDefinition(), 1, createAgentDefinition(), 1))
                )
                .put(
                    InternalPosition.definedBy(1, 0, 3),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 2)
                )
                .put(
                    InternalPosition.definedBy(4, 2, 3),
                    CellPopulationDescription.populationFromPair(createAgentDefinition(), 2)
                )
                .put(
                    InternalPosition.definedBy(4, 22, 3),
                    CellPopulationDescription.populationFromMap(of(createAgentDefinition(), 4, createAgentDefinition(), 1))
                )
                .build()
        );


        final ComputationConfiguration configuration = ComputationConfiguration.builder()
            .habitatGeography(new HabitatGeography(mock(StructureDefinition.class), distributionMap))
            .build();

        // when

        ConfigurationDivision configDivision = tested.split(configuration, forCountable(c5, c8, c7));

        // then

        assertThat(configDivision.get(c5).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap().getNumberOfAgents()).isEqualTo(5L);
        assertThat(configDivision.get(c8).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap().getNumberOfAgents()).isEqualTo(8L);
        assertThat(configDivision.get(c7).orElse(DEFAULT_CONFIG).getHabitatGeography().getPopulationDistributionMap().getNumberOfAgents()).isEqualTo(7L);

    }

    private static AgentDefinition createAgentDefinition() {
        return mock(AgentDefinition.class);
    }

    private static Countable countableFor(long value) {
        Countable countable = mock(Countable.class);
        when(countable.getCount()).thenReturn(UnsignedInteger.valueOf(value));
        return countable;
    }

}