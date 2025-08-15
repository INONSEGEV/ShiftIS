package com.example.myapplication;

import android.net.Uri;
import java.util.List;

public class ProblemItem {
    private String title;
    private String topic;
    private String subTopic;
    private String description;
    private String remark;
    private List<Uri> images;

    public ProblemItem(String title) {
        this.title = title;
    }

    // getters & setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getSubTopic() { return subTopic; }
    public void setSubTopic(String subTopic) { this.subTopic = subTopic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public List<Uri> getImages() { return images; }
    public void setImages(List<Uri> images) { this.images = images; }
}
