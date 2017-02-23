package org.naturenet.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.common.base.Strings;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.naturenet.R;
import org.naturenet.data.model.Observation;
import org.naturenet.util.NatureNetUtils;

public class ObservationAdapter extends FirebaseListAdapter<Observation> {

    ObservationAdapter(Activity activity, Query query) {
        super(activity, Observation.class, R.layout.observation_list_item, query);
    }

    @Override
    protected void populateView(final View v, final Observation model, int position) {
        v.setTag(model);
        ViewGroup badge = (ViewGroup) v.findViewById(R.id.observation_user_badge);
        //TODO: instead of recreating a new layout, make a Badge class and clear contents individually
        badge.removeAllViews();
        NatureNetUtils.makeUserBadge(mActivity, badge, model.userId);
        Picasso.with(mActivity).load(Strings.emptyToNull(model.data.image)).error(R.drawable.no_image)
                .fit().centerCrop().into((ImageView) v.findViewById(R.id.observation_icon));
    }

    @Override
    public Observation getItem(int pos) {
        return super.getItem(getCount() - 1 - pos);
    }
}