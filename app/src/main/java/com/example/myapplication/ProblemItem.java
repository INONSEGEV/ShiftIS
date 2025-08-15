package com.example.myapplication;

import java.io.Serializable;

public class ProblemItem implements Serializable {
    private String title;
    private String topic;
    private String subTopic;
    private String description;
    private String remark;
    private String date;         // שדה תאריך
    private boolean expanded;    // מצב פתיחה/סגירה של השורה

    // בנאי עם כותרת בלבד
    public ProblemItem(String title) {
        this.title = title;
    }

    // בנאי מלא
    public ProblemItem(String title, String topic, String subTopic, String description, String remark, String date) {
        this.title = title;
        this.topic = topic;
        this.subTopic = subTopic;
        this.description = description;
        this.remark = remark;
        this.date = date;
        this.expanded = false;
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

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
}
