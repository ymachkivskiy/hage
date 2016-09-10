package org.hage.mocked.simdata.state;

import lombok.Data;
import org.hage.platform.simulation.runtime.state.descriptor.CloneableValue;

import java.io.Serializable;

@Data
public class CustomState implements Serializable, CloneableValue<CustomState> {
    private int algaeAmount;

    public int reduceAmountAndGet(int algaeAmount) {
        this.algaeAmount -= algaeAmount;
        return this.algaeAmount;
    }

    public CustomState withIncreasedAmount(int algaeAmount) {
        this.algaeAmount += algaeAmount;
        return this;
    }

    @Override
    public CustomState createClone() {
        CustomState customState = new CustomState();
        customState.setAlgaeAmount(algaeAmount);
        return customState;
    }
}
