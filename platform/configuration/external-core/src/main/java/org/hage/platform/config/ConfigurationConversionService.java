package org.hage.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.loader.Configuration;
import org.hage.platform.habitat.structure.BoundaryConditions;
import org.hage.platform.habitat.structure.Dimensions;
import org.hage.platform.habitat.structure.StructureDefinition;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

//This is temporary mechanism, used until final user config file will be designed and computation configuration loading implemented
@Deprecated
@Slf4j
public class ConfigurationConversionService {

    private static final HabitatExternalConfiguration HABITAT_EXTERNAL_CONFIGURATION = new HabitatExternalConfiguration(
            new StructureDefinition(Dimensions.of(3, 3, 3), BoundaryConditions.CLOSED, BigDecimal.TEN),
            emptyList() //TODO do not forget about it

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

        List<IComponentDefinition> localComponents = componentDefinitionCollection.stream().filter(this::isLocalComponent).collect(toList());
        List<IComponentDefinition> globalComponents = componentDefinitionCollection.stream().filter(this::isGlobalComponent).collect(toList());


        log.info("Creating habitat configuration {}", HABITAT_EXTERNAL_CONFIGURATION);

        return Configuration
                .builder()
                .localComponents(localComponents)
                .globalComponents(globalComponents)
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
