package com.example.expensemanagementapp;

import java.util.List;

public class AmountAndDescription {
    String incomingAmount;
    String outgoingAmount;
    List<String> description;
    public AmountAndDescription()
    {
    }
    public AmountAndDescription(String incomingAmount,String outgoingAmount, List<String> description)
    {
        this.incomingAmount = incomingAmount;
        this.outgoingAmount = outgoingAmount;
        this.description = description;
    }
    public String getIncomingAmount() {
        return incomingAmount;
    }

    public void setIncomingAmount(String incomingAmount) {
        this.incomingAmount = incomingAmount;
    }

    public String getOutgoingAmount() {
        return outgoingAmount;
    }

    public void setOutgoingAmount(String outgoingAmount) {
        this.outgoingAmount = outgoingAmount;
    }

    public List<String> getDescription() {
        return description;
    }
}
