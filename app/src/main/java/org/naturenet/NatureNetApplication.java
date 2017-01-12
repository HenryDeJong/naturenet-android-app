package org.naturenet;

import android.app.Application;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.naturenet.data.model.Project;
import org.naturenet.data.model.Site;
import org.naturenet.data.model.Users;
import org.naturenet.util.ForestFire;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class NatureNetApplication extends Application {

    private final BehaviorSubject<Optional<Users>>  usersBehaviorSubject = BehaviorSubject.create();

    public Observable<Optional<Users>> getCurrentUserObservable() {
        return usersBehaviorSubject;
    }

    private FirebaseAuth.AuthStateListener mAuthStateListener = firebaseAuth -> {
        if(firebaseAuth.getCurrentUser() != null) {
            final String uid = firebaseAuth.getCurrentUser().getUid();
            Timber.i("User logged in: %s", uid);
            FirebaseDatabase.getInstance().getReference(Users.NODE_NAME).child(uid).keepSynced(true);
            FirebaseDatabase.getInstance().getReference(Users.NODE_NAME).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Timber.d("Loaded profile data for %s" + uid);
                    usersBehaviorSubject.onNext(Optional.fromNullable(dataSnapshot.getValue(Users.class)));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Timber.e("Could not load user data for %s: %s", uid);
                    usersBehaviorSubject.onNext(Optional.absent());
                    Toast.makeText(NatureNetApplication.this, getString(R.string.login_error_message_firebase_read), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Timber.i("User logged out");
            usersBehaviorSubject.onNext(Optional.absent());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new ForestFire());
        Picasso.with(this).setIndicatorsEnabled(BuildConfig.DEBUG);
        FirebaseDatabase.getInstance().setLogLevel(BuildConfig.DEBUG ? Logger.Level.DEBUG : Logger.Level.NONE);
        FirebaseDatabase.getInstance().getReference(Site.NODE_NAME).keepSynced(true);
        FirebaseDatabase.getInstance().getReference(Project.NODE_NAME).keepSynced(true);
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }
}