package com.example.myapplication.Recommendations;

import android.os.Parcel;
import android.os.Parcelable;

public class RecommendationsItem implements Parcelable {

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

    // ----------------------------
    // Parcelable implementation
    // ----------------------------
    protected RecommendationsItem(Parcel in) {
        description = in.readString();
        amount = in.readString();
        unitPrice = in.readString();
        unit = in.readString();
        totalPrice = in.readString();
    }

    public static final Creator<RecommendationsItem> CREATOR = new Creator<RecommendationsItem>() {
        @Override
        public RecommendationsItem createFromParcel(Parcel in) {
            return new RecommendationsItem(in);
        }

        @Override
        public RecommendationsItem[] newArray(int size) {
            return new RecommendationsItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(amount);
        dest.writeString(unitPrice);
        dest.writeString(unit);
        dest.writeString(totalPrice);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // ----------------------------
    // Getters & Setters
    // ----------------------------
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getUnitPrice() { return unitPrice; }
    public void setUnitPrice(String unitPrice) { this.unitPrice = unitPrice; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }
}
