package org.hage.platform.component.structure.connections;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;
import org.hage.platform.component.structure.StructureDefinition;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class StructureCoreConfigurer extends CoreConfigurerAdapter<StructureDefinition> {

    @Autowired
    private ConfigurableStructure configurableStructure;

    public StructureCoreConfigurer(int order) {
        super(config -> config.getCommon().getStructureDefinition(), order);
    }

    @Override
    protected void configureWithNarrow(StructureDefinition narrowConfiguration) {
        log.debug("Configure structure");
        configurableStructure.configure(narrowConfiguration);
    }

}
