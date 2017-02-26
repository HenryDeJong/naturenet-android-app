package org.naturenet;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.naturenet.data.model.UsersPrivate;

import static org.junit.Assert.assertEquals;

/**
 * Created by rigot on 2/18/2017.
 */

@RunWith(AndroidJUnit4.class)
public class UsersPrivateParcelizationTests {

    static String ID = "id";
    static String NAME = "name";

    @Test
    public void is_user_private_parcelable(){

        UsersPrivate u = UsersPrivate.createNew(ID, NAME);

        Parcel parcel = Parcel.obtain();

        u.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        UsersPrivate parceledPrivateUser = UsersPrivate.CREATOR.createFromParcel(parcel);

        assertEquals(null, parceledPrivateUser.id, u.id);
        assertEquals(null, parceledPrivateUser.name, u.name);

    }
}
