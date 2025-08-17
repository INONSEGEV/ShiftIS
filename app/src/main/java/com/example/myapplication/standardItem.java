package com.example.myapplication;

import android.net.Uri;
import java.util.ArrayList;

public class standardItem {
    private String standard;
    private ArrayList<Uri> images;

    public standardItem(String standard) {
        this.standard = standard;
        this.images = new ArrayList<>();
    }

    public standardItem(String standard, ArrayList<Uri> images) {
        this.standard = standard;
        this.images = images != null ? images : new ArrayList<>();
    }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public ArrayList<Uri> getImages() { return images; }
    public void setImages(ArrayList<Uri> images) { this.images = images; }
}
