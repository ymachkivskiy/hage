package org.hage.platform.component.simulationconfig;

import org.springframework.core.Ordered;

public interface CoreConfigurer extends Ordered {
    void configureWith(Configuration configuration);
}
