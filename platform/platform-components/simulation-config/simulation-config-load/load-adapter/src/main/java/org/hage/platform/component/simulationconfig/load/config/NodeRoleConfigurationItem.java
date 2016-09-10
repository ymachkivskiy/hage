package org.hage.platform.component.simulationconfig.load.config;

import com.google.common.collect.ImmutableSet;
import org.hage.platform.config.ConfigurationItem;
import org.hage.platform.config.ConfigurationItemProperties;
import org.hage.platform.config.ConfigurationValueCheckException;

import java.util.Objects;
import java.util.Set;

class NodeRoleConfigurationItem extends ConfigurationItem {

    private static final String MASTER_ROLE = "master";
    private static final String SLAVE_ROLE = "slave";

    private final Set<String> allowedValues = ImmutableSet.of(MASTER_ROLE, SLAVE_ROLE);

    public NodeRoleConfigurationItem() {
        super(new ConfigurationItemProperties(
            false,
            "r",
            "simulation-role",
            "Node simulation role. Determines whether this node should be master or slave. Possible values {" + MASTER_ROLE + "|" + SLAVE_ROLE + "}",
            true,
            String.class,
            "ROLE",
            "slave"
        ));
    }

    public boolean isMasterRole(String role) {
        return MASTER_ROLE.equals(role);
    }

    @Override
    public void checkValue(Object defaultValue) throws ConfigurationValueCheckException {
        if (!(defaultValue instanceof String) || !allowedValues.contains(defaultValue)) {
            throw new ConfigurationValueCheckException("Illegal value of simulation role : '" + Objects.toString(defaultValue) + "'. Allowed values are: " + allowedValues);
        }
    }
}
