package com.example.myapplication.carrier;

import android.content.Context;
import android.content.Intent;

import com.example.myapplication.CarrierRow.CarrierRowItem;
import com.example.myapplication.Problems;

import java.util.ArrayList;

public class carrierItem {
    private String carrierName;
    boolean isExpanded = false; // <-- סטטוס פתוח/סגור

    private ArrayList<CarrierRowItem> innerItems;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setInnerItems(ArrayList<CarrierRowItem> innerItems) {
        this.innerItems = innerItems;
    }

    public carrierItem(String carrierName) {
        this.carrierName = carrierName;
        this.innerItems = new ArrayList<>();
    }
    public carrierItem(String carrierName, ArrayList<CarrierRowItem> innerItems) {
        this.carrierName = carrierName;
        this.innerItems = innerItems;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public ArrayList<CarrierRowItem> getInnerItems() {
        return innerItems;
    }

    // פונקציה חדשה להחזרת Intent לעריכת Carrier
    public Intent getEditIntent(Context context, int position) {
        Intent intent = new Intent(context, Problems.class);
        intent.putExtra("position", position);
        intent.putExtra("carrierName", carrierName);
        return intent;
    }
}
