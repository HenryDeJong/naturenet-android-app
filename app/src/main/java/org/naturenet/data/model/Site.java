package org.naturenet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Site implements Parcelable {

    @Exclude
    public static final String NODE_NAME = "sites";

    public String id;

    public String name;

    public String description;

    @PropertyName("l")
    public ArrayList<Double> location = Lists.newArrayList();

    @PropertyName("g")
    public String geohash;

    private Site() {}

    public Site(String id, String name, String description, ArrayList<Double> location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
    }

    protected Site(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        in.readList(this.location, null);
        this.geohash = in.readString();
    }

    public static final Creator<Site> CREATOR = new Creator<Site>() {
        @Override
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        @Override
        public Site[] newArray(int size) {
            return new Site[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeList(location);
        parcel.writeString(geohash);
    }
}