package org.hage.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.definition.IComponentDefinition;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

//This is temporary mechanism, used until final user config file will be designed and computation configuration loading implemented
//TODO do not forget about it
@Deprecated
@Slf4j
public class ConfigurationConversionService {

    private final Collection<Class<?>> localComponents = new HashSet<>();

    public ConfigurationConversionService() {
        try {
            this.localComponents.add(Class.forName("org.hage.workplace.Workplace"));
        } catch (ClassNotFoundException e) {
            log.error("Error while converting component definition into computation config", e);
            System.exit(-1);
        }
    }

    public ComputationConfiguration convert(Collection<IComponentDefinition> componentDefinitionCollection) {

        List<IComponentDefinition> localComponents = componentDefinitionCollection.stream().filter(this::isLocalComponent).collect(toList());
        List<IComponentDefinition> globalComponents = componentDefinitionCollection.stream().filter(this::isGlobalComponent).collect(toList());

        return ComputationConfiguration
                .builder()
                .localComponents(localComponents)
                .globalComponents(globalComponents)
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
