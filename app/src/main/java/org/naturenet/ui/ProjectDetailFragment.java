package org.naturenet.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.naturenet.R;
import org.naturenet.data.model.Observation;
import org.naturenet.data.model.Project;

import timber.log.Timber;

public class ProjectDetailFragment extends Fragment {

    static String COMPLETED = "Completed";
    static String FRAGMENT_TAG = "project_detail_fragment";
    private static final String ARG_PROJECT = "ARG_PROJECT";

    private TextView mName, mStatus, mDescription, mEmpty;
    private ImageView mIcon, mStatusIcon;
    private GridView mGvObservations;
    private Project mProject;

    public static ProjectDetailFragment newInstance(Project p) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROJECT, p);
        ProjectDetailFragment frag = new ProjectDetailFragment();
        frag.setArguments(args);
        frag.setRetainInstance(true);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mName = (TextView) view.findViewById(R.id.project_tv_name);
        mStatus = (TextView) view.findViewById(R.id.project_tv_status);
        mDescription = (TextView) view.findViewById(R.id.project_tv_description);
        mEmpty = (TextView) view.findViewById(R.id.project_tv_no_recent_contributions);
        mIcon = (ImageView) view.findViewById(R.id.project_iv_icon);
        mStatusIcon = (ImageView) view.findViewById(R.id.project_iv_status);
        mGvObservations = (GridView) view.findViewById(R.id.observation_gallery);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() == null || getArguments().getParcelable(ARG_PROJECT) == null) {
            Timber.e(new IllegalArgumentException(), "Tried to load ProjectDetailFragment without a Project argument");
            Toast.makeText(getActivity(), "No project to display", Toast.LENGTH_SHORT).show();
            return;
        }
        mProject = getArguments().getParcelable(ARG_PROJECT);

        mName.setText(mProject.name);

        if (mProject.status != null) {
            mStatus.setText(mProject.status);

            if (mProject.status.equals(COMPLETED)) {
                mStatusIcon.setVisibility(View.VISIBLE);
            } else {
                mStatusIcon.setVisibility(View.GONE);
            }
        } else {
            mStatusIcon.setVisibility(View.GONE);
        }

        if (mProject.description != null) {
            mDescription.setText(mProject.description);
            mDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        }
        if (mProject.iconUrl != null) { Picasso.with(getActivity()).load(Strings.emptyToNull(mProject.iconUrl)).fit().into(mIcon); }

        Query query = FirebaseDatabase.getInstance().getReference(Observation.NODE_NAME)
                .orderByChild("activity").equalTo(mProject.id).limitToLast(20);
        ObservationAdapter adapter = new ObservationAdapter(getActivity(), query);
        mGvObservations.setAdapter(adapter);
        mGvObservations.setEmptyView(mEmpty);

        mGvObservations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent observationIntent = new Intent(getActivity(), ObservationActivity.class);
                observationIntent.putExtra(ObservationActivity.EXTRA_OBSERVATION, (Observation)view.getTag());
                startActivity(observationIntent);
            }
        });
    }
}