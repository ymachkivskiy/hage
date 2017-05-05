package org.hage.platform.node.runtime.stateprops;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.simconf.CoreConfigurerAdapter;
import org.hage.platform.simulation.runtime.state.UnitPropertiesStateComponent;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class UnitPropertiesCoreConfigurer extends CoreConfigurerAdapter<Class<? extends UnitPropertiesStateComponent>> {

    @Autowired
    private UnitPropertiesContainerProvider unitPropertiesContainerProvider;

    public UnitPropertiesCoreConfigurer(int order) {
        super(config -> config.getCommon().getUnitPropertiesConfiguratorClazz(), order);
    }

    @Override
    protected void configureWithNarrow(Class<? extends UnitPropertiesStateComponent> unitPropsComponentClazz) {
        log.debug("Configure unit properties.");
        unitPropertiesContainerProvider.configure(unitPropsComponentClazz);
    }

}
