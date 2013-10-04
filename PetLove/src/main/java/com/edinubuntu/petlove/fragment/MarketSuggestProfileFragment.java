package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Record;
import com.squareup.picasso.Picasso;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketSuggestProfileFragment extends SherlockFragment {

    private Record record;

    public static MarketSuggestProfileFragment newInstance(Record record) {
        MarketSuggestProfileFragment fragment = new MarketSuggestProfileFragment();
        fragment.record = record;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.record_profile_page, container, false);

        ImageView petImageView = (ImageView)view.findViewById(R.id.pet_image_view);

        Picasso.with(getSherlockActivity())
                .load(record.getImageName())
                .into(petImageView);

        return view;
    }
}
