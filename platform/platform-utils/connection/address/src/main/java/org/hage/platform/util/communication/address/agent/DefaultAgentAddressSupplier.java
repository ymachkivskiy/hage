package org.hage.platform.util.communication.address.agent;


import org.hage.platform.util.communication.address.node.NodeAddressSupplier;
import org.hage.platform.util.container.share.SharedBetweenContainers;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;


@SharedBetweenContainers
@ThreadSafe
public class DefaultAgentAddressSupplier implements AgentAddressSupplier {

    private static final String DEFAULT_NAME_TEMPLATE = "agent*";
    private final String nameTemplate;
    private final AtomicLong nameTemplateCounter = new AtomicLong();

    @Inject
    private NodeAddressSupplier nodeAddressSupplier;

    public DefaultAgentAddressSupplier() {
        this(DEFAULT_NAME_TEMPLATE);
    }

    public DefaultAgentAddressSupplier(final String nameTemplate) {
        this.nameTemplate = checkNotNull(nameTemplate);
    }

    @Override
    public DefaultAgentAddress get() {
        return new DefaultAgentAddress(nodeAddressSupplier.get(), generateName());
    }

    private String generateName() {
        return nameTemplate.replace("*", Long.toString(nameTemplateCounter.getAndIncrement()));
    }
}
