package org.naturenet.data.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class Observation extends TimestampedData {

    @Exclude
    public static final String NODE_NAME = "observations";

    public static Observation createNew(String id, String observer, String projectId, String siteId, String where, List<Double> location, PhotoCaptionContent data) {
        Observation o = new Observation();
        o.id = id;
        o.userId = observer;
        o.projectId = projectId;
        o.siteId = siteId;
        o.where = where;
        o.location = location;
        o.data = data;
        o.source = "android";
        return o;
    }

    public String id;

    @PropertyName("observer")
    public String userId;

    @PropertyName("activity")
    public String projectId;

    @PropertyName("site")
    public String siteId;

    @PropertyName("g")
    public String geohash;

    @PropertyName("l")
    public List<Double> location = Lists.newArrayList();

    public PhotoCaptionContent data;

    @Nullable
    public String where;

    @Nullable
    public String status;

    @Nullable
    public String source;

    @Nullable
    public Map<String, Boolean> comments = Maps.newHashMap();

    @Nullable
    public Map<String, Boolean> likes = Maps.newHashMap();

    public Observation() {}

    private Observation(Parcel in) {
        super(in);
        this.id = in.readString();
        this.userId = in.readString();
        this.projectId = in.readString();
        this.siteId = in.readString();
        this.geohash = in.readString();
        in.readList(this.location, null);
        this.data = in.readParcelable(PhotoCaptionContent.class.getClassLoader());
        this.where = in.readString();
        this.status = in.readString();
        this.source = in.readString();
        in.readMap(this.comments, null);
        in.readMap(this.likes, null);
    }

    @Exclude
    public boolean isValid() { return !"deleted".equalsIgnoreCase(status); }

    @Exclude
    public Double getLatitude() {
        if (location != null && location.size() == 2) {
            return location.get(0);
        }

        return null;
    }

    @Exclude
    public Double getLongitude() {
        if (location != null && location.size() == 2) {
            return location.get(1);
        }

        return null;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(projectId);
        parcel.writeString(siteId);
        parcel.writeString(geohash);
        parcel.writeList(location);
        parcel.writeParcelable(data, flags);
        parcel.writeString(where);
        parcel.writeString(status);
        parcel.writeString(source);
        parcel.writeMap(comments);
        parcel.writeMap(likes);
    }

    public static final Creator<Observation> CREATOR = new Creator<Observation>() {
        @Override
        public Observation createFromParcel(Parcel in) {
            return new Observation(in);
        }

        @Override
        public Observation[] newArray(int size) {
            return new Observation[size];
        }
    };

    public PhotoCaptionContent getData() {
        return data;
    }

    @Nullable
    public String getWhere() {
        return where;
    }
}