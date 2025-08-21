package com.example.myapplication.carrier;

import com.example.myapplication.CarrierRow.CarrierRowItem;

import java.util.ArrayList;

public class carrierItem {
    private String carrierName;
    private ArrayList<CarrierRowItem> innerItems;

    public void setCarrier(String carrierName) {
        this.carrierName = carrierName;
    }

    public carrierItem(String carrierName) {
        this.carrierName = carrierName;
        this.innerItems = new ArrayList<>();
    }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }

    public String getCarrierName() { return carrierName; }
    public ArrayList<CarrierRowItem> getInnerItems() { return innerItems; }
}
