package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.R;

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
                FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new RecordsFragment()).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
