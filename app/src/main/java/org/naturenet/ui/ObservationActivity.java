package org.naturenet.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
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
import org.naturenet.data.model.ObserverInfo;
import org.naturenet.data.model.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ObservationActivity extends AppCompatActivity {
    static String FRAGMENT_TAG_OBSERVATION_GALLERY = "observation_gallery_fragment";
    static String FRAGMENT_TAG_OBSERVATION = "observation_fragment";
    static String TITLE = "EXPLORE";
    static String SIGNED_USER = "signed_user";
    static String OBSERVERS = "observers";
    static String OBSERVATION = "observation";
    static String OBSERVATIONS = "observations";
    static String EMPTY = "";
    Toolbar toolbar;
    Observation selectedObservation;
    ObserverInfo selectedObserverInfo;
    TextView explore_tv_back, toolbar_title;
    ImageButton back;
    GridView gridView;
    DatabaseReference mFirebase;
    List<Observation> observations;
    List<ObserverInfo> observers;
    List<Comment> comments;
    Users signed_user;
    Boolean like;
    @Override
    protected void onSaveInstanceState(Bundle outState) {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) findViewById(R.id.app_bar_explore_tv);
        explore_tv_back = (TextView) findViewById(R.id.explore_tv_back);
        back = (ImageButton) findViewById(R.id.explore_b_back);
        gridView = (GridView) findViewById(R.id.observation_gallery);
        signed_user = (Users) getIntent().getSerializableExtra(SIGNED_USER);
        observations = (ArrayList<Observation>) getIntent().getSerializableExtra(OBSERVATIONS);
        observers = (ArrayList<ObserverInfo>) getIntent().getSerializableExtra(OBSERVERS);
        setSupportActionBar(toolbar);
        toolbar.setTitle(EMPTY);
        selectedObservation = null;
        selectedObserverInfo = null;
        comments = null;
        if (getIntent().getSerializableExtra(OBSERVATION) != null) {
            selectedObservation = (Observation) getIntent().getSerializableExtra(OBSERVATION);
            for (int i=0; i<observers.size(); i++) {
                if (observers.get(i).getObserverId().equals(selectedObservation.userId)) {
                    selectedObserverInfo = observers.get(i);
                    break;
                }
            }
            goToSelectedObservationFragment();
        } else
            goToObservationGalleryFragment();
        explore_tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToObservationGalleryFragment();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToExploreFragment();
            }
        });
    }
    public void goBackToExploreFragment() {
        observations = null;
        observers = null;
        selectedObservation = null;
        selectedObserverInfo = null;
        comments = null;
        gridView = null;
        Intent resultIntent = new Intent(this, MainActivity.class);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }
    public void goToObservationGalleryFragment() {
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar_title.setText(TITLE);
        back.setVisibility(View.VISIBLE);
        explore_tv_back.setVisibility(View.GONE);
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new ObservationGalleryFragment(), FRAGMENT_TAG_OBSERVATION_GALLERY).
                addToBackStack(null).
                commit();
    }
    public void goToSelectedObservationFragment() {
        toolbar_title.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        explore_tv_back.setVisibility(View.VISIBLE);
        comments = null;
        like = null;
        if (selectedObservation.comments != null) {
            getCommentsFor(selectedObservation.id);
        }
        if (signed_user != null) {
            like = (selectedObservation.likes != null) && selectedObservation.likes.keySet().contains(signed_user.id);
        }
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new ObservationFragment(), FRAGMENT_TAG_OBSERVATION).
                addToBackStack(null).
                commit();
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
    public void goBackToObservationGalleryFragment() {
        selectedObservation = null;
        selectedObserverInfo = null;
        comments = null;
        goToObservationGalleryFragment();
    }
}