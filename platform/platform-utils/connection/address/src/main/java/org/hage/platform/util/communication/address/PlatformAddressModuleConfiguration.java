package org.hage.platform.util.communication.address;

import org.hage.platform.util.communication.address.agent.DefaultAgentAddressSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformAddressModuleConfiguration {

    @Bean
    public DefaultAgentAddressSupplier getAgentAddressSupplier() {
        return new DefaultAgentAddressSupplier();
    }

}
