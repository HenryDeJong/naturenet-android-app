package org.naturenet.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naturenet.R;
import org.naturenet.data.model.Observation;
import org.naturenet.data.model.Project;
import org.naturenet.data.model.Users;

import java.util.Collections;
import java.util.List;

public class AddObservationActivity extends AppCompatActivity {

    public static final String EXTRA_OBSERVATION = "observation";
    public static final String EXTRA_IMAGE_PATH = "observation_path";
    public static final String EXTRA_USER = "signed_user";

    static String EMPTY = "";

    Uri observationPath;
    Observation newObservation;
    Users signedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(EMPTY);
        setSupportActionBar(toolbar);
        newObservation = getIntent().getParcelableExtra(EXTRA_OBSERVATION);
        observationPath = getIntent().getParcelableExtra(EXTRA_IMAGE_PATH);
        signedUser = getIntent().getParcelableExtra(EXTRA_USER);
        getFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, new AddObservationFragment(), AddObservationFragment.FRAGMENT_TAG).
                commit();
    }

    public void goBackToMainActivity() {
        findViewById(R.id.toolbar_send).setVisibility(View.GONE);
        findViewById(R.id.toolbar_busy).setVisibility(View.VISIBLE);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(EXTRA_OBSERVATION, newObservation);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}