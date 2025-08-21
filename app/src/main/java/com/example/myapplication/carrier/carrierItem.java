package com.example.myapplication.carrier;

import android.content.Context;
import android.content.Intent;

import com.example.myapplication.CarrierRow.CarrierRowItem;
import com.example.myapplication.Problems;

import java.util.ArrayList;

public class carrierItem {
    private String carrierName;
    private ArrayList<CarrierRowItem> innerItems;

    public carrierItem(String carrierName) {
        this.carrierName = carrierName;
        this.innerItems = new ArrayList<>();
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
