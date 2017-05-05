package org.hage.platform.node.structure.connections;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simconf.CoreConfigurerAdapter;
import org.hage.platform.node.structure.StructureDefinition;
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
