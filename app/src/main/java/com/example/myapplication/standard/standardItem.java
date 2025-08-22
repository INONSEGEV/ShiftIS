package com.example.myapplication.standard;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class standardItem implements Parcelable {
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

    protected standardItem(Parcel in) {
        standard = in.readString();
        images = in.createTypedArrayList(Uri.CREATOR);
    }

    public static final Creator<standardItem> CREATOR = new Creator<standardItem>() {
        @Override
        public standardItem createFromParcel(Parcel in) {
            return new standardItem(in);
        }

        @Override
        public standardItem[] newArray(int size) {
            return new standardItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(standard);
        dest.writeTypedList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public ArrayList<Uri> getImages() { return images; }
    public void setImages(ArrayList<Uri> images) { this.images = images; }
}
