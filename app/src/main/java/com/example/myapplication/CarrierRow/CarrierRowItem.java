package com.example.myapplication.CarrierRow;

import android.net.Uri;
import com.example.myapplication.Recommendations.RecommendationsItem;
import com.example.myapplication.standard.standardItem;

import java.util.ArrayList;

public class CarrierRowItem {

    private String carrier;
    private String subTopic;
    private String description;
    private String remark;
    private ArrayList<standardItem> standard;
    private ArrayList<RecommendationsItem> recommendations;
    private ArrayList<Uri> images;

    public CarrierRowItem(String carrier, String subTopic) {
        this.carrier = carrier;
        this.subTopic = subTopic;
        this.description = "";
        this.remark = "";
        this.standard = new ArrayList<>();
        this.recommendations = new ArrayList<>();
        this.images = new ArrayList<>();
    }

    public String getCarrier() { return carrier; }
    public String getSubTopic() { return subTopic; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public void setSubTopic(String subTopic) { this.subTopic = subTopic; }
}
