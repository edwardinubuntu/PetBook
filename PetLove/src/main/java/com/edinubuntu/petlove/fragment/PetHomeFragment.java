package com.edinubuntu.petlove.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.activity.MarketActivity;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class PetHomeFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_home_empty, container, false);

        Button pickPetButton = (Button)rootView.findViewById(R.id.pet_home_go_pick_button);
        pickPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marketIntent = new Intent(getSherlockActivity(), MarketActivity.class);
                startActivity(marketIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
