package org.hage.platform.simconf;

import org.springframework.core.Ordered;

public interface CoreConfigurer extends Ordered {

    void configureWith(Configuration configuration);

    default int getOrder() {
        return 0;
    }

}
