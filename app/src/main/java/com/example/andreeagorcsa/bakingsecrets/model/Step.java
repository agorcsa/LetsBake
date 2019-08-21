package com.example.andreeagorcsa.bakingsecrets.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Step extends ArrayList<Parcelable> implements Parcelable {
    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
    @SerializedName("id")
    private Integer stepId;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    // step constructor
    public Step(Integer stepId, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.stepId = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    // reads from parcel
    protected Step(Parcel in) {
        if (in.readByte() == 0) {
            stepId = null;
        } else {
            stepId = in.readInt();
        }
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // writes to parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (stepId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(stepId);
        }
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    // Getter and Setter methods
    public Integer getStepsId() {
        return stepId;
    }

    public void setStepsId(Integer stepsId) {
        this.stepId = stepsId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}

