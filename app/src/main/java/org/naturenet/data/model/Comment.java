package org.naturenet.data.model;

import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Comment extends TimestampedData {

    @Exclude
    public static final String NODE_NAME = "comments";

    public static Comment createNew(String id, String comment, String commenter, String parent, String context) {
        Comment c = new Comment();
        c.id = id;
        c.comment = comment;
        c.commenter = commenter;
        c.parent = parent;
        c.context = context;
        c.source = "android";
        return c;
    }

    public String id;

    public String comment;

    public String commenter;

    public String parent;

    public String context;

    @Nullable
    public String source;

    @Nullable
    public String status;

    private Comment() {}

    private Comment(Parcel in) {
        super(in);
        this.id = in.readString();
        this.comment = in.readString();
        this.commenter = in.readString();
        this.parent = in.readString();
        this.context = in.readString();
    }

    @Exclude
    public boolean isValid() {
        return !"deleted".equalsIgnoreCase(status);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(id);
        parcel.writeString(comment);
        parcel.writeString(commenter);
        parcel.writeString(parent);
        parcel.writeString(context);
        parcel.writeString(source);
        parcel.writeString(status);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}