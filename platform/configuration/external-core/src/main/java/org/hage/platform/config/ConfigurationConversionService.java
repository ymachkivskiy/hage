package org.hage.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.HabitatOrganizationDefinition;
import org.hage.platform.config.def.agent.ChunkAgentDistribution;
import org.hage.platform.config.loader.Configuration;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.hage.platform.config.def.agent.AgentCountData.fixed;
import static org.hage.platform.config.def.agent.PositionsSelectionData.allPositions;

//This is temporary mechanism, used until final user config file will be designed and computation configuration loading implemented
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
            this.localComponents.add(Class.forName("org.hage.workplace.Workplace"));
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
