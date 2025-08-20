package com.example.myapplication.carrier;

import com.example.myapplication.CarrierRow.CarrierRowItem;
import java.util.ArrayList;
import java.util.List;

public class carrierItem {
    private String carrier;
    private List<CarrierRowItem> items;

    public carrierItem(String carrier) {
        this.carrier = carrier;
        this.items = new ArrayList<>();
    }

    public String getCarrier() { return carrier; }
    public List<CarrierRowItem> getItems() { return items; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public void addItem(CarrierRowItem item) { items.add(item); }
    public void removeItem(int index) { if(index>=0 && index<items.size()) items.remove(index); }
}
