package org.hage.platform.config.load.xml;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.component.rate.model.ComputationRatingConfig;
import org.hage.platform.component.rate.model.MeasurerRateConfig;
import org.hage.platform.component.rate.model.MeasurerType;
import org.hage.platform.config.load.definition.ChunkPopulationQualifier;
import org.hage.platform.config.load.definition.HabitatOrganizationDefinition;
import org.hage.platform.config.load.definition.InputConfiguration;
import org.hage.platform.config.load.definition.agent.ChunkAgentDistribution;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.EnumSet;

import static java.util.Collections.singletonList;
import static org.hage.platform.config.load.definition.agent.AgentCountData.fixed;
import static org.hage.platform.config.load.definition.agent.PositionsSelectionData.allPositions;

//This is temporary mechanism, used until final user config file will be designed and computation configuration loading implemented
@Component
@Deprecated
@Slf4j
public class ConfigurationConversionService {

    private static final HabitatOrganizationDefinition HABITAT_EXTERNAL_CONFIGURATION = new HabitatOrganizationDefinition(
        new StructureDefinition(Dimensions.of(2, 2, 1), BoundaryConditions.CLOSED),
        singletonList(
            new ChunkPopulationQualifier(
                new Chunk(Position.ZERO, Dimensions.of(2, 2, 1)),
                singletonList(
                    new ChunkAgentDistribution(
                        null,
                        fixed(3),
                        allPositions()
                    )
                )
            )
        )

    );

    public InputConfiguration convert(Collection<IComponentDefinition> componentDefinitionCollection) {
        log.info("Creating habitat configuration {}", HABITAT_EXTERNAL_CONFIGURATION);

        return InputConfiguration
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

}
