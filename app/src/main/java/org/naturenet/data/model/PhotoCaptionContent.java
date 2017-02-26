package org.naturenet.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class PhotoCaptionContent implements Parcelable {

    public String image;
    public String text;

    public PhotoCaptionContent(String im, String txt){
        image = im;
        text =txt;
    }

    public PhotoCaptionContent() {}

    private PhotoCaptionContent(Parcel in) {
        image = in.readString();
        text = in.readString();
    }

    public static final Creator<PhotoCaptionContent> CREATOR = new Creator<PhotoCaptionContent>() {
        @Override
        public PhotoCaptionContent createFromParcel(Parcel in) {
            return new PhotoCaptionContent(in);
        }

        @Override
        public PhotoCaptionContent[] newArray(int size) {
            return new PhotoCaptionContent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(image);
        parcel.writeString(text);
    }
}