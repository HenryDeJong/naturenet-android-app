package org.naturenet;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.data.model.PhotoCaptionContent;

import static org.junit.Assert.assertEquals;

/**
 * Created by rigot on 2/18/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PhotoCaptionContentParcelizationTests {

    static String IMAGE_URL = "www.firebaseimage.com";
    static String TEXT = "trees";

    @Test
    public void test_is_photoCaptionContent_parcelable(){

        PhotoCaptionContent photo = new PhotoCaptionContent(IMAGE_URL, TEXT);

        Parcel parcel = Parcel.obtain();

        photo.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        PhotoCaptionContent parcelPhoto = PhotoCaptionContent.CREATOR.createFromParcel(parcel);

        assertEquals(null, parcelPhoto.image, IMAGE_URL);
        assertEquals(null, parcelPhoto.text, TEXT);

    }
}
