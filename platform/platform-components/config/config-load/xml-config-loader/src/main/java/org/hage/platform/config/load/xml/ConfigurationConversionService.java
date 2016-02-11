package org.hage.platform.config.load.xml;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;
import org.hage.platform.config.load.definition.Configuration;
import org.hage.platform.config.load.definition.HabitatOrganizationDefinition;
import org.hage.platform.config.load.definition.agent.ChunkAgentDistribution;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.*;
import org.hage.platform.rate.model.ComputationRatingConfig;
import org.hage.platform.rate.model.MeasurerRateConfig;
import org.hage.platform.rate.model.MeasurerType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hage.platform.config.load.definition.agent.AgentCountData.fixed;
import static org.hage.platform.config.load.definition.agent.PositionsSelectionData.allPositions;

//This is temporary mechanism, used until final user config file will be designed and computation configuration loading implemented
@Component
@Deprecated
@Slf4j
public class ConfigurationConversionService {

    private static class FakeAgent{
        public void step() {
            System.out.println("Hello from fake agent");
        }
    }

    private static final HabitatOrganizationDefinition HABITAT_EXTERNAL_CONFIGURATION = new HabitatOrganizationDefinition(
        new StructureDefinition(Dimensions.of(2, 2, 1), BoundaryConditions.CLOSED, BigDecimal.TEN),
        singletonList(
            new ChunkPopulationQualifier(
                new Chunk(InternalPosition.ZERO, Dimensions.of(2, 2, 1)),
                singletonList(
                    new ChunkAgentDistribution(
                        new AgentDefinition() {
                            @Override
                            public String getName() {
                                return "fakeAgent";
                            }

                            @Override
                            public Class<?> getType() {
                                return FakeAgent.class;
                            }

                            @Override
                            public List<Type> getTypeParameters() {
                                return emptyList();
                            }

                            @Override
                            public boolean isSingleton() {
                                return false;
                            }

                            @Override
                            public List<IArgumentDefinition> getConstructorArguments() {
                                return emptyList();
                            }

                            @Override
                            public List<IComponentDefinition> getInnerComponentDefinitions() {
                                return emptyList();
                            }
                        },
                        fixed(3),
                        allPositions()
                    )
                )
            )
        )

    );

    private final Collection<Class<?>> localComponents = new HashSet<>();

    public ConfigurationConversionService() {
        try {
            this.localComponents.add(Class.forName("org.hage.platform.component.workplace.Workplace"));
        } catch (ClassNotFoundException e) {
            log.error("Error while converting component definition into computation config", e);
            System.exit(-1);
        }
    }

    public Configuration convert(Collection<IComponentDefinition> componentDefinitionCollection) {
        log.info("Creating habitat configuration {}", HABITAT_EXTERNAL_CONFIGURATION);

        return Configuration
            .builder()
            .globalComponents(componentDefinitionCollection)
            .habitatConfiguration(HABITAT_EXTERNAL_CONFIGURATION)
            .computationRatingConfig(
                    ComputationRatingConfig.builder()
                        .enabledRateMeasureTypes(EnumSet.of(MeasurerType.RAM_MEMORY))
                        .measurerRateConfigs(singletonList(
                            new MeasurerRateConfig(MeasurerType.RAM_MEMORY, 2, 5)
                    )).build()
            )
            .build();
    }

    private boolean isLocalComponent(IComponentDefinition componentDefinition) {
        return localComponents
            .stream()
            .filter(lc -> lc.isAssignableFrom(componentDefinition.getType()))
            .findFirst()
            .isPresent();
    }

    private boolean isGlobalComponent(IComponentDefinition componentDefinition) {
        return !isLocalComponent(componentDefinition);
    }

}
