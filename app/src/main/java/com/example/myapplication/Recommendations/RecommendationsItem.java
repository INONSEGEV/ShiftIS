package com.example.myapplication.Recommendations;

public class RecommendationsItem {

    private String description;
    private String amount;
    private String unitPrice;
    private String unit;
    private String totalPrice;

    public RecommendationsItem(String description, String amount, String unitPrice, String unit, String totalPrice) {
        this.description = description;
        this.amount = amount;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
