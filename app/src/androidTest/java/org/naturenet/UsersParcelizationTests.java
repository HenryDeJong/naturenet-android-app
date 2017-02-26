package org.naturenet;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.data.model.Users;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by rigot on 2/18/2017.
 */

@RunWith(AndroidJUnit4.class)
public class UsersParcelizationTests {

    static String ID = "id";
    static String DISPLAY_NAME = "displayName";
    static String AFFILIATION = "affiliation";
    static String AVATAR = "Avatar";

    @Test
    public void testing_users() {

        Map<String, Boolean> map = new HashMap<>();

        map.put("foo", false);
        map.put("bar",true);
        Users user = Users.createNew(ID, DISPLAY_NAME, AFFILIATION, AVATAR);
        Parcel parcel = Parcel.obtain();
        user.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Users parcelUser = user.CREATOR.createFromParcel(parcel);

        assertEquals(null, parcelUser.id, ID);
        assertEquals(null, parcelUser.displayName, DISPLAY_NAME);
        assertEquals(null, parcelUser.affiliation, AFFILIATION);
        assertEquals(null, parcelUser.avatar, AVATAR);

    }
}
