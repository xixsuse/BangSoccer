package upgrade.ntv.bangsoccer.TourList;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import upgrade.ntv.bangsoccer.ActivityMain;
import upgrade.ntv.bangsoccer.AppConstants.Constants;
import upgrade.ntv.bangsoccer.Attraction.Attraction;
import upgrade.ntv.bangsoccer.R;
import upgrade.ntv.bangsoccer.Utils.Utils;


/**
 * The tourist attraction detail fragment which contains the details of a
 * a single attraction (contained inside
 * {@link upgrade.ntv.bangsoccer.TourList.FragmentAttractionDetail
 * }).
 */
public class FragmentAttractionDetail extends Fragment {

    private static final String EXTRA_ATTRACTION = "attraction";
    private Attraction mAttraction;

    public FragmentAttractionDetail() {
    }

    public static FragmentAttractionDetail createInstance(String attractionName) {
        FragmentAttractionDetail detailFragment = new FragmentAttractionDetail();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ATTRACTION, attractionName);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.content_attraction_detail, container, false);
        String attractionName = getArguments().getString(EXTRA_ATTRACTION);
        mAttraction = ActivityMain.mAttractionsArrayList.get(0);

        if (mAttraction == null) {
            getActivity().finish();
            return null;
        }

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView descTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        FloatingActionButton mapFab = (FloatingActionButton) view.findViewById(R.id.mapFab);

        LatLng location = Utils.getLocation(getActivity());
        String distance = Utils.formatDistanceBetween(location, mAttraction.getGeo());
        if (TextUtils.isEmpty(distance)) {
            distanceTextView.setVisibility(View.GONE);
        }

        nameTextView.setText(attractionName);
        distanceTextView.setText(distance);
        descTextView.setText(mAttraction.getLongDescription());

        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size)
                * Constants.IMAGE_ANIM_MULTIPLIER;
        Glide.with(getActivity())
                .load(R.drawable.puerto_atlantico)  //TODO: attraction object needs to hold and int value instead of a drawable
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.lighter_gray)
                .override(imageSize, imageSize)
                .into(imageView);

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.MAPS_INTENT_URI +
                        Uri.encode(mAttraction.getName() + ", " + mAttraction.getAreaId())));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Some small additions to handle "up" navigation correctly
                Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Check if up activity needs to be created (usually when
                // detail screen is opened from a notification or from the
                // Wearable app
                if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)
                        || getActivity().isTaskRoot()) {

                    // Synthesize parent stack
                    TaskStackBuilder.create(getActivity())
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // On Lollipop+ we finish so to run the nice animation
                    getActivity().finishAfterTransition();
                    return true;
                }

                // Otherwise let the system handle navigating "up"
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Really hacky loop for finding attraction in our static content provider.
     * Obviously would not be used in a production app.
     */
    private Attraction findAttraction(String attractionName) {
        for (Map.Entry<String, List<Attraction>> attractionsList : TouristAttractions.ATTRACTIONS.entrySet()) {
            List<Attraction> attractions = attractionsList.getValue();
            for (Attraction attraction : attractions) {
                if (attractionName.equals(attraction.getName())) {
                    return ActivityMain.mAttractionsArrayList.get(0);
                }
            }
        }
        return null;
    }
}