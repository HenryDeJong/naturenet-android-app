package org.naturenet;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.data.model.Observation;
import org.naturenet.data.model.PhotoCaptionContent;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by rigot on 2/12/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ObservationClassTests {


    static String ID = "id";
    static String OBSERVER = "observer";
    static String PROJECT_ID = "projectid";
    static String SITE_ID = "siteid";
    static String WHERE = "where";
    static String IMAGE = "image";
    static String TEXT = "text";

    @Test
    public void test_is_observation_parcelable(){


        ArrayList<Double> list = new ArrayList<Double>();
        list.add(43.5);
        list.add(94.3);
        list.add(23.3);

        PhotoCaptionContent captionContent = new PhotoCaptionContent(IMAGE, TEXT);


        Observation observation = Observation.createNew(ID, OBSERVER, PROJECT_ID, SITE_ID, WHERE, list, captionContent);

        Parcel parcel = Parcel.obtain();

        observation.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        Observation parceledObservation = Observation.CREATOR.createFromParcel(parcel);

        assertEquals(null, parceledObservation.id, ID);
        assertEquals(null, parceledObservation.userId, OBSERVER);
        assertEquals(null, parceledObservation.projectId, PROJECT_ID);
        assertEquals(null, parceledObservation.siteId, SITE_ID);
        assertEquals(null, parceledObservation.where, WHERE);
        assertEquals(null, parceledObservation.location, list);
        assertEquals(null, parceledObservation.getData().text, captionContent.text);
        assertEquals(null, parceledObservation.getData().image, captionContent.image);
    }

}
