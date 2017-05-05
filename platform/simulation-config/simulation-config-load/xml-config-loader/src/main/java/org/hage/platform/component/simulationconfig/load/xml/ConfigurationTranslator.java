package org.hage.platform.component.simulationconfig.load.xml;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.loadbalance.config.BalanceMode;
import org.hage.platform.cluster.loadbalance.config.LoadBalanceConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.rate.model.MeasurerType;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.ControlAgentDefinition;
import org.hage.platform.component.simulationconfig.load.definition.ChunkPopulationQualifier;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.hage.platform.component.simulationconfig.load.definition.SimulationOrganizationDefinition;
import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;
import org.hage.platform.component.simulationconfig.load.definition.agent.ChunkAgentDistribution;
import org.hage.platform.component.simulationconfig.load.definition.agent.PositionsSelectionData;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.structure.grid.GridBoundaryConditions;
import org.hage.platform.component.structure.grid.GridNeighborhoodType;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

// FIXME: 06.04.17 this is temporary solution
// TODO: 06.04.17 really, this is temporary, do not be scarred
@SingletonComponent
public class ConfigurationTranslator {

    public InputConfiguration translateToInternal(HageConfiguration configuration) {

        return InputConfiguration.builder()
                // fixme: currently not supported
                .globalComponents(emptyList())
                .loadBalanceConfig(getLoadBalanceConfig(configuration))
                .computationRatingConfig(getComputationRatingConfig(configuration))
                .stopConditionClazz(getStopConditionClazz(configuration))
                .propertiesConfiguratorClazz(getPropertiesConfiguratorClazz(configuration))
                .simulationDefinition(getSimulationDefinition(configuration))
                .build();
    }

    private SimulationOrganizationDefinition getSimulationDefinition(HageConfiguration configuration) {
        return new SimulationOrganizationDefinition(
                mapStructureDefinition(configuration.getSimulationConfig().getEnvironmentStructure()),
                mapAgentDefinitions(configuration.getSimulationConfig().getEnvironmentPopulationConfig()),
                mapControlAgentDefinition(configuration.getSimulationConfig().getEnvironmentPopulationConfig()),
                mapChunkPopulationQualifiers(configuration.getSimulationConfig())
        );
    }

    private List<ChunkPopulationQualifier> mapChunkPopulationQualifiers(SimulationConfigurationType simConfig) {
        return simConfig.getEnvironmentPopulationConfig().getRegionPopulationQualifier()
                .stream()
                .map(q -> {
                            Chunk chunk = mapRegion(q.getRegion(), simConfig.getEnvironmentStructure().getGrid().getGridDimensions());

                            AgentCountData agentCountData = mapAgentCellPopulator(q.getAgentCellPopulator());
                            PositionsSelectionData positionsSelectionData = mapCellsSelector(q.getCellSelector());

                            List<ChunkAgentDistribution> chunkAgentDistributions = q.getAgents()
                                    .stream()
                                    .map(this::mapAgentDefitionType)
                                    .map(ad -> new ChunkAgentDistribution(ad, agentCountData, positionsSelectionData))
                                    .collect(toList());


                            return new ChunkPopulationQualifier(chunk, chunkAgentDistributions);

                        }

                ).collect(toList());

    }


    private Chunk mapRegion(PopulationRegionDescriptorType regionDescriptorType, Dimensions gridDimensions) {
        if (regionDescriptorType.getFullGridArea() != null) {
            return new Chunk(org.hage.platform.component.structure.Position.ZERO, mapDimensions(gridDimensions));
        }else
        {
            return new Chunk(mapPosition(regionDescriptorType.getPosition()), mapDimensions(regionDescriptorType.getDimensions()));
        }
    }


    private AgentCountData mapAgentCellPopulator(RegionPopulationQualifierType.AgentCellPopulator cellPopulator) {
        if (cellPopulator.getRandomNumber() != null) {
            return AgentCountData.random();
        } else if (cellPopulator.getFixedNumber() != null) {
            return AgentCountData.fixed(cellPopulator.getFixedNumber());
        } else {
            Integer atLeast = ofNullable(cellPopulator.getRangeNumber().atLeast).orElse(1);
            Integer atMost = ofNullable(cellPopulator.getRangeNumber().atMost).orElse(100000);
            return AgentCountData.between(atLeast, atMost);
        }
    }

    private PositionsSelectionData mapCellsSelector(RegionPopulationQualifierType.CellSelector cellSelector) {

        if (cellSelector.getAllCells() != null) {
            return  PositionsSelectionData.allPositions();
        } else {
            if (cellSelector.getRandom().count != null) {
                return PositionsSelectionData.randomPositions(cellSelector.getRandom().count);
            }else{
                return PositionsSelectionData.randomPositions();
            }
        }
    }


    private org.hage.platform.component.structure.Position mapPosition(Position position) {
        return org.hage.platform.component.structure.Position.position(position.getDepth(), position.getHorizontal(), position.getVertical());
    }

    private ControlAgentDefinition mapControlAgentDefinition(EnvironmentPopulationConfigType environmentPopulationConfig) {
        return ofNullable(environmentPopulationConfig.getDefinitions().getControlAgentDefinition())
                .map(this::mapControlAgentDef)
                .orElse(null);
    }

    private ControlAgentDefinition mapControlAgentDef(ControlAgentDefinitionType controlAgentDefinitionType) {
        String agentClass = controlAgentDefinitionType.getAgentClass();

        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(agentClass);

            return new ControlAgentDefinition((Class<? extends ControlAgent>) aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<AgentDefinition>  mapAgentDefinitions(EnvironmentPopulationConfigType environmentPopulationConfig) {

        return environmentPopulationConfig.getDefinitions().getAgentDefinition()
                .stream()
                .map(this::mapAgentDefitionType)
                .collect(toList());
    }

    private AgentDefinition mapAgentDefitionType(AgentDefinitionType agentDefinitionType) {

        String agentClass = agentDefinitionType.getAgentClass();


        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(agentClass);

            return new AgentDefinition((Class<? extends Agent>) aClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private org.hage.platform.component.structure.StructureDefinition mapStructureDefinition(StructureDefinition environmentStructure) {
        return new org.hage.platform.component.structure.StructureDefinition(
                mapBoundaryConditions(environmentStructure.getGrid().getBoundaryConditionsType()),
                mapDimensions(environmentStructure.getGrid().getGridDimensions()),
                mapNeighborhoodType(environmentStructure.getGrid().getNeighborhoodType())
        );
    }

    private GridBoundaryConditions mapBoundaryConditions(BoundaryConditionsType boundaryConditionsType) {
        if (boundaryConditionsType == BoundaryConditionsType.CLOSED) {
            return GridBoundaryConditions.CLOSED;

        } else if (boundaryConditionsType == BoundaryConditionsType.DEPTH_TORUS) {
            return GridBoundaryConditions.DEPTH__TORUS;

        } else if (boundaryConditionsType == BoundaryConditionsType.HORIZONTAL_TORUS) {
            return GridBoundaryConditions.HORIZONTAL__TORUS;

        } else if (boundaryConditionsType == BoundaryConditionsType.VERTICAL_TORUS) {
            return GridBoundaryConditions.VERTICAL__TORUS;

        } else if (boundaryConditionsType == BoundaryConditionsType.DEPTH_AND_HORIZONTAL_TORUS) {
            return GridBoundaryConditions.DEPTH_AND_HORIZONTAL__TORUS;

        } else if (boundaryConditionsType == BoundaryConditionsType.DEPTH_AND_VERTICAL_TORUS) {
            return GridBoundaryConditions.DEPTH_AND_VERTICAL__TORUS;

        } else if (boundaryConditionsType == BoundaryConditionsType.HORIZONTAL_AND_VERTICAL_TORUS) {
            return GridBoundaryConditions.HORIZONTAL_AND_VERTICAL__TORUS;

        } else  {
            return GridBoundaryConditions.FULL__TORUS;
        }
    }

    private GridNeighborhoodType mapNeighborhoodType(NeighborhoodType neighborhoodType) {
        if (neighborhoodType == NeighborhoodType.NO_NEIGHBORS) {
            return GridNeighborhoodType.NO_NEIGHBORS;

        } else if (neighborhoodType == NeighborhoodType.VON_NEUMANN_NEIGHBORHOOD) {
            return GridNeighborhoodType.VON_NEUMANN_NEGIHBORHOOD;

        } else {
            return GridNeighborhoodType.MOORE_NEIGHBORHOOD;
        }
    }

    private org.hage.platform.component.structure.grid.Dimensions mapDimensions(Dimensions dimensions) {
        return org.hage.platform.component.structure.grid.Dimensions.definedBy(dimensions.getDepthSize(), dimensions.getHorizontalSize(), dimensions.getVerticalSize());
    }

    private org.hage.platform.component.rate.model.ComputationRatingConfig getComputationRatingConfig(HageConfiguration configuration) {
        return ofNullable(configuration.getPlatformConfig())
                    .map(PlatformConfigType::getComputationRateConfig)
                    .map(this::mapComputationRateConfig)
                    // default value
                    .orElseGet(() -> org.hage.platform.component.rate.model.ComputationRatingConfig.builder().enabledRateMeasureTypes(emptySet()).measurerRateConfigs(emptyList()).build());
    }

    private LoadBalanceConfig getLoadBalanceConfig(HageConfiguration configuration) {
        return ofNullable(configuration.getPlatformConfig())
                    .map(PlatformConfigType::getBalanceConfig)
                    .map(this::mapBalanceConfig)
                    // default value
                    .orElseGet(() -> new LoadBalanceConfig(BalanceMode.DISABLED, 0));
    }

    private LoadBalanceConfig mapBalanceConfig(org.hage.platform.component.simulationconfig.load.xml.LoadBalanceConfig xmlLoadBalanceConfig) {

        if (xmlLoadBalanceConfig.getDisabled() != null) {
            return new LoadBalanceConfig(BalanceMode.DISABLED, 0);
        }

        org.hage.platform.component.simulationconfig.load.xml.LoadBalanceConfig.AmountType amountType = xmlLoadBalanceConfig.getAmountType();

        if (amountType.getMode() == org.hage.platform.component.simulationconfig.load.xml.BalanceMode.AFTER_STEP_COUNT) {
            return new LoadBalanceConfig(BalanceMode.AFTER_STEP_COUNT, amountType.getValue());
        } else  {
            return new LoadBalanceConfig(BalanceMode.AFTER_SECONDS_PASSED, amountType.getValue());
        }

    }

    private org.hage.platform.component.rate.model.ComputationRatingConfig mapComputationRateConfig(ComputationRatingConfig computationRatingConfig) {
        Set<MeasurerType> enabledMeasurers = computationRatingConfig.getEnabledRateMeasurers()
                .stream()
                .map(this::mapMeasurerType)
                .collect(toSet());

        List<MeasurerRateConfig> measurerRateConfigs = computationRatingConfig.getMeasurerRateConfigs()
                .stream()
                .map(c -> new MeasurerRateConfig(mapMeasurerType(c.getMeasuredType()), c.getRateWeight(), c.getMaxRate()))
                .collect(toList());

        return org.hage.platform.component.rate.model.ComputationRatingConfig
                .builder()
                .enabledRateMeasureTypes(enabledMeasurers)
                .measurerRateConfigs(measurerRateConfigs)
                .build();

    }


    private MeasurerType mapMeasurerType(MeasurerTypeType type) {
        if (type == MeasurerTypeType.CONCURRENCY) {
            return MeasurerType.CONCURRENCY;

        } else  {
            return MeasurerType.RAM_MEMORY;
        }
    }

    private Class<? extends StopCondition> getStopConditionClazz(HageConfiguration configuration) {
        SimulationConfigurationType.StopCondition stopCondition = configuration.getSimulationConfig().getStopCondition();

        String stopConditionClass = stopCondition.getStopConditionClass();

        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(stopConditionClass);

            return (Class<? extends StopCondition>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Class<? extends UnitPropertiesStateComponent> getPropertiesConfiguratorClazz(HageConfiguration configuration) {
        SimulationConfigurationType.PropertiesConfigurator propertiesConfigurator = configuration.getSimulationConfig().getPropertiesConfigurator();

        String stopConditionClass = propertiesConfigurator.getConfiguratorClass();

        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(stopConditionClass);

            return (Class<? extends UnitPropertiesStateComponent>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
