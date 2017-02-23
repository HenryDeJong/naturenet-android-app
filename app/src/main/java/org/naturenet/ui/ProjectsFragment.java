package org.naturenet.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.naturenet.R;
import org.naturenet.data.model.Project;

import timber.log.Timber;

public class ProjectsFragment extends Fragment {

    public static final String FRAGMENT_TAG = "projects_fragment";
    static String LATEST_CONTRIBUTION = "latest_contribution";

    MainActivity main;
    private ListView mProjectsListView = null;
    private DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
    private FirebaseListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);
        main = ((MainActivity) getActivity());
        TextView toolbar_title = (TextView) main.findViewById(R.id.app_bar_main_tv);
        toolbar_title.setText(R.string.projects_title);

        mProjectsListView = (ListView) root.findViewById(R.id.projects_list);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Timber.d("Getting projects");
        Query query = mFirebase.child(Project.NODE_NAME).orderByChild(LATEST_CONTRIBUTION);
        mAdapter = new ProjectAdapter(main, query);
        mProjectsListView.setAdapter(mAdapter);

        mProjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                main.goToProjectActivity((Project)view.getTag());
            }
        });
    }
}