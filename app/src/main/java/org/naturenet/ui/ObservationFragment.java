package org.naturenet.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.naturenet.R;
import org.naturenet.data.model.Comment;
import org.naturenet.data.model.Observation;
import org.naturenet.data.model.Site;
import org.naturenet.data.model.Users;
import org.naturenet.util.NatureNetUtils;

import timber.log.Timber;

public class ObservationFragment extends Fragment {

    public static final String FRAGMENT_TAG = "observation_fragment";
    private static final String ARG_OBSERVATION = "ARG_OBSERVATION";

    ObservationActivity o;
    ImageView observer_avatar, observation_image, like;
    TextView observer_name, observer_affiliation, observeration_timestamp, observeration_text, send;
    EditText comment;
    ListView lv_comments;
    private String mObservationId;

    public static ObservationFragment newInstance(String observationId) {
        ObservationFragment frag = new ObservationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OBSERVATION, observationId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_observation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        observer_avatar = (ImageView) view.findViewById(R.id.selected_observer_avatar);
        observation_image = (ImageView) view.findViewById(R.id.selected_observation_icon);
        like = (ImageView) view.findViewById(R.id.iv_like);
        observer_name = (TextView) view.findViewById(R.id.selected_observer_user_name);
        observer_affiliation = (TextView) view.findViewById(R.id.selected_observer_affiliation);
        observeration_timestamp = (TextView) view.findViewById(R.id.selected_observeration_timestamp);
        observeration_text = (TextView) view.findViewById(R.id.selected_observation_text);
        send = (TextView) view.findViewById(R.id.tv_send);
        comment = (EditText) view.findViewById(R.id.et_comment);
        lv_comments = (ListView) view.findViewById(R.id.lv_comments);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() == null || getArguments().getString(ARG_OBSERVATION) == null) {
            Timber.e(new IllegalArgumentException(), "Tried to load ObservationFragment without an Observation argument");
            Toast.makeText(getActivity(), "No observation to display", Toast.LENGTH_SHORT).show();
            return;
        }

        o = (ObservationActivity) getActivity();
        mObservationId = getArguments().getString(ARG_OBSERVATION);

        FirebaseDatabase.getInstance().getReference(Observation.NODE_NAME).child(mObservationId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Observation obs = dataSnapshot.getValue(Observation.class);

                Picasso.with(getActivity()).load(Strings.emptyToNull(obs.data.image)).placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image).fit().centerInside().into(observation_image);

                if (obs.data.text != null) {
                    observeration_text.setText(obs.data.text);
                } else {
                    observeration_text.setText(R.string.no_description);
                }

                observeration_timestamp.setText(NatureNetUtils.toDateString(obs));

                FirebaseDatabase.getInstance().getReference(Users.NODE_NAME).child(obs.userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Users user = dataSnapshot.getValue(Users.class);
                                NatureNetUtils.showUserAvatar(getActivity(), observer_avatar, user.avatar);
                                if (user.displayName != null) {
                                    observer_name.setText(user.displayName);
                                } else {
                                    observer_name.setText(R.string.unknown_user);
                                }

                                FirebaseDatabase.getInstance().getReference(Site.NODE_NAME).child(user.affiliation)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Site site = dataSnapshot.getValue(Site.class);
                                                if (site.name != null) {
                                                    observer_affiliation.setText(site.name);
                                                } else {
                                                    observer_affiliation.setText(null);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Timber.w(databaseError.toException(), "Unable to read data for site %s", user.affiliation);
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Timber.w(databaseError.toException(), "Unable to read data for user %s", obs.userId);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.w(databaseError.toException(), "Unable to read data for observation %s", mObservationId);
            }
        });

        if (o.like != null) {
            if (o.like) {
                like.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.like));
            } else {
                like.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.unlike));
            }
        } else {
            like.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.unlike));
        }

        if (o.comments != null) {
            Timber.d("Comments are available");
            for (Comment c : o.comments) {
                Timber.d("Comment: s", c.toString());
            }
        } else {
            Timber.d("Comments are not available");
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o.signed_user != null) {
                    Optional<Boolean> value;

                    if (o.like) {
                        o.like = false;
                        like.setImageDrawable(ContextCompat.getDrawable(ObservationFragment.this.getActivity(), R.drawable.unlike));
                        value = Optional.absent();
                    } else {
                        o.like = true;
                        like.setImageDrawable(ContextCompat.getDrawable(ObservationFragment.this.getActivity(), R.drawable.like));
                        value = Optional.of(true);
                    }

                    DatabaseReference fbRef = FirebaseDatabase.getInstance().getReference();
                    fbRef.child("observations").child(o.selectedObservation.id).child("likes").child(o.signed_user.id).setValue(value.orNull());
                } else
                    Toast.makeText(o, "Please login to like an observation.", Toast.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = comment.getText().toString();

                if (!commentText.isEmpty()) {
                    if (o.signed_user != null) {
                        send.setEnabled(false);
                        comment.setEnabled(false);
                        final DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child(Comment.NODE_NAME).push();
                        Comment newComment = Comment.createNew(commentRef.getKey(), commentText, o.signed_user.id, o.selectedObservation.id, Observation.NODE_NAME);

                        commentRef.setValue(newComment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                send.setEnabled(true);
                                comment.setEnabled(true);

                                if (databaseError != null) {
                                    Timber.w("Could not write comment for %s: %s", o.selectedObservation.id, databaseError.getDetails());
                                    Toast.makeText(o, "Your comment could not be submitted.", Toast.LENGTH_LONG).show();
                                } else {
                                    // Update /observations/<observation-id>/comments/ with new comment
                                    FirebaseDatabase.getInstance().getReference().child(Observation.NODE_NAME)
                                            .child(o.selectedObservation.id).child("comments").child(commentRef.getKey()).setValue(true);
                                    comment.getText().clear();
                                    Toast.makeText(o, "Your comment has been submitted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(o, "Please login to comment.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}