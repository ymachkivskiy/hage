package org.hage.platform.simconf.load.generate.count;

import org.hage.platform.simconf.load.definition.agent.AgentCountData;

import java.util.EnumSet;
import java.util.Random;

abstract class AbstractCountProvider implements AgentCountProvider {

    private final EnumSet<ValueType> obligatoryValues;
    protected Random rand = new Random();

    protected AbstractCountProvider() {
        this.obligatoryValues = EnumSet.noneOf(ValueType.class);
    }

    protected AbstractCountProvider(ValueType firstObligatory, ValueType... otherObligatoryValues) {
        this.obligatoryValues = EnumSet.of(firstObligatory, otherObligatoryValues);
    }

    @Override
    public final Integer getAgentCount(AgentCountData countData) {

        if (obligatoryValues.contains(ValueType.PRIMARY) && !countData.getValue().isPresent()) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires primary value");
        }

        if (obligatoryValues.contains(ValueType.SECONDARY) && !countData.getSecondaryValue().isPresent()) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires secondary value");
        }

        return getCountInternal(countData);
    }

    protected abstract Integer getCountInternal(AgentCountData countData);

    enum ValueType {
        PRIMARY,
        SECONDARY
    }
}
