package org.hage.example.state;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomState implements Serializable {
    private int algaeAmount;

    public int reduceAmountAndGet(int algaeAmount) {
        this.algaeAmount -= algaeAmount;
        return this.algaeAmount;
    }

    public CustomState withIncreasedAmount(int algaeAmount) {
        this.algaeAmount += algaeAmount;
        return this;
    }

}
