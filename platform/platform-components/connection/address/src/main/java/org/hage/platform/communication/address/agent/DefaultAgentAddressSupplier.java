package org.hage.platform.communication.address.agent;


import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.util.container.share.SharedBetweenContainers;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;


@SharedBetweenContainers
@ThreadSafe
public class DefaultAgentAddressSupplier implements AgentAddressSupplier {

    private static final String DEFAULT_NAME_TEMPLATE = "agent*";
    private final String nameTemplate;
    private final AtomicLong nameTemplateCounter = new AtomicLong();
//
//    @Inject
//    private LocalNodeAddressSupplier nodeAddressSupplier;

    public DefaultAgentAddressSupplier() {
        this(DEFAULT_NAME_TEMPLATE);
    }

    public DefaultAgentAddressSupplier(final String nameTemplate) {
        this.nameTemplate = checkNotNull(nameTemplate);
    }

    // TODO: 10.03.16 dont forget
    @Override
    public DefaultAgentAddress get() {
        return new DefaultAgentAddress(new NodeAddress() {
            @Override
            public String getUniqueIdentifier() {
                return "Dummy";
            }

            @Override
            public String toString() {
                return getUniqueIdentifier();
            }
        }, generateName());
    }

    private String generateName() {
        return nameTemplate.replace("*", Long.toString(nameTemplateCounter.getAndIncrement()));
    }
}
