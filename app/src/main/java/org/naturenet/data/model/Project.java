package org.naturenet.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.Map;

@IgnoreExtraProperties
public class Project implements Parcelable {

    public Project(String id, String iconUrl, String description, String name,  String status, Long latestContribution, Map<String, Boolean> sites) {
        this.sites = sites;
        this.latestContribution = latestContribution;
        this.status = status;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.id = id;
    }

    @Exclude
    public static final String NODE_NAME = "activities";

    public String id;

    @PropertyName("icon_url")
    public String iconUrl;

    public String description;

    public String name;

    @Nullable
    public String status;

    @Nullable
    @PropertyName("latest_contribution")
    public Long latestContribution;

    @Nullable
    public Map<String, Boolean> sites = Maps.newHashMap();

    private Project() {}

    private Project(Parcel in) {
        this.id = in.readString();
        this.iconUrl = in.readString();
        this.description = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.latestContribution = in.readLong();
        in.readMap(this.sites, null);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(iconUrl);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeLong(latestContribution == null ? 0L : latestContribution);
        parcel.writeMap(sites);
    }
}