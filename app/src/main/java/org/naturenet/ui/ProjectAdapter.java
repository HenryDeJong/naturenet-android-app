package org.naturenet.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.common.base.Strings;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.naturenet.R;
import org.naturenet.data.model.Project;

public class ProjectAdapter extends FirebaseListAdapter<Project> {

    public ProjectAdapter(Activity activity, Query query) {
        super(activity, Project.class, R.layout.project_list_item, query);
    }

    @Override
    protected void populateView(final View v, final Project model, int position) {
        v.setTag(model);
        ImageView thumbnail = (ImageView) v.findViewById(R.id.project_thumbnail);
        Picasso.with(mActivity).load(Strings.emptyToNull(model.iconUrl)).fit().into(thumbnail);
        TextView name = (TextView) v.findViewById(R.id.project_name);
        name.setText(model.name);
    }

    @Override
    public Project getItem(int pos) {
        return super.getItem(getCount() - 1 - pos);
    }
}