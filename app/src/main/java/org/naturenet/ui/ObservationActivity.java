package org.naturenet.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naturenet.R;
import org.naturenet.data.model.Comment;
import org.naturenet.data.model.Observation;
import org.naturenet.data.model.Users;

import java.util.List;

import timber.log.Timber;

public class ObservationActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "signed_user";
    public static final String EXTRA_OBSERVATION = "observation";

    Observation selectedObservation;
    GridView gridView;
    DatabaseReference mFirebase;
    List<Comment> comments;
    Users signed_user;
    Boolean like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        selectedObservation = getIntent().getParcelableExtra(EXTRA_OBSERVATION);
        signed_user = getIntent().getParcelableExtra(EXTRA_USER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(R.string.observation_title);
        }

        gridView = (GridView) findViewById(R.id.observation_gallery);
        comments = null;

        goToSelectedObservationFragment();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToSelectedObservationFragment() {
        comments = null;
        like = null;

        if (selectedObservation.comments != null) { getCommentsFor(selectedObservation.id); }

        if (signed_user != null) {
            like = (selectedObservation.likes != null) && selectedObservation.likes.keySet().contains(signed_user.id);
        }

        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, ObservationFragment.newInstance(selectedObservation.id), ObservationFragment.FRAGMENT_TAG)
                .commit();
    }

    private void getCommentsFor(final String parent) {
        comments = Lists.newArrayList();
        mFirebase = FirebaseDatabase.getInstance().getReference();
        mFirebase.child(Comment.NODE_NAME).orderByChild("parent").equalTo(parent).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    comments.add(child.getValue(Comment.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.w("Could not load comments for record %s, query canceled: %s", parent, databaseError.getDetails());
                Toast.makeText(ObservationActivity.this, "Unable to load comments for this observation.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}