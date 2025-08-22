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
    private String date;
    private ArrayList<standardItem> standard;
    private ArrayList<RecommendationsItem> recommendations;
    private ArrayList<Uri> images;

    public CarrierRowItem(String carrier, String subTopic, String description, String remark, String date, ArrayList<standardItem> standard, ArrayList<RecommendationsItem> recommendations, ArrayList<Uri> images) {
        this.carrier = carrier;
        this.subTopic = subTopic;
        this.description = description;
        this.remark = remark;
        date = date;
        this.standard = standard;
        this.recommendations = recommendations;
        this.images = images;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        date = date;
    }

    public ArrayList<standardItem> getStandard() {
        return standard;
    }

    public void setStandard(ArrayList<standardItem> standard) {
        this.standard = standard;
    }

    public ArrayList<RecommendationsItem> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(ArrayList<RecommendationsItem> recommendations) {
        this.recommendations = recommendations;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }
}
