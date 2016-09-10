package org.hage.platform.component.runtime.container;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.init.ContainerConfiguration;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ContainerCoreConfigurer extends CoreConfigurerAdapter<ContainerConfiguration> {

    @Autowired
    private GlobalInstanceContainerController globalInstanceContainerController;

    public ContainerCoreConfigurer(int order) {
        super(config -> config.getCommon().getContainerConfiguration(), order);
    }

    @Override
    protected void configureWithNarrow(ContainerConfiguration containerConfig) {
        log.debug("Configure container");
        globalInstanceContainerController.configure(containerConfig);
    }
}
