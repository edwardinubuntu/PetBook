package com.edinubuntu.petlove.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.activity.MarketActivity;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.Pet;
import com.edinubuntu.petlove.object.User;
import com.edinubuntu.petlove.service.AlarmBroadcastReceiver;
import com.edinubuntu.petlove.util.manager.UserManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class PetHomeFragment extends SherlockFragment {

    public static final long MINUTES_TO_VISIT_AGAIN = 60L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_home, container, false);

        Button pickPetButton = (Button)rootView.findViewById(R.id.pet_home_go_pick_button);
        pickPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMarket();
            }
        });

        return rootView;
    }

    private void goMarket() {
        // Check visit event
        final Event lastVisitEvent = new Select().from(Event.class).where("Action = 'VISIT_MARKET_SUGGESTIONS' or Action = 'VISIT_MARKET_QUESTIONS_ANSWER'").orderBy("id DESC").executeSingle();

        Date now = Calendar.getInstance().getTime();
        if (lastVisitEvent == null ||  now.getTime() - lastVisitEvent.getCreatedDate().getTime() > 1000L * 60L * MINUTES_TO_VISIT_AGAIN) {
            Intent marketIntent = new Intent(getSherlockActivity(), MarketActivity.class);
            startActivity(marketIntent);
        } else {
            // Alert in one hour
            new AlertDialog.Builder(getSherlockActivity())
                    .setTitle(getString(R.string.market_enter_block_title))
                    .setMessage(getString(R.string.market_enter_block_message))
                    .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // create the object
                            AlarmManager alarmManager = (AlarmManager)getSherlockActivity().getSystemService(Context.ALARM_SERVICE);

                            Intent intentAlarm = new Intent(getSherlockActivity(), AlarmBroadcastReceiver.class);

                            Calendar cal = Calendar.getInstance(); // creates calendar
                            cal.setTime(lastVisitEvent.getCreatedDate()); // sets calendar time/date
                            cal.add(Calendar.MINUTE, (int)MINUTES_TO_VISIT_AGAIN); // adds one hour

                            // set the alarm for particular time
                            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                    PendingIntent.getBroadcast(getSherlockActivity(), 1,
                                            intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        User currentPlayer = UserManager.getCurrentPlayer();
        if (currentPlayer != null) {
            getView().findViewById(R.id.pet_home_empty_layout).setVisibility(
                    currentPlayer.getActivePets().isEmpty() ? View.VISIBLE : View.INVISIBLE);
            getView().findViewById(R.id.pet_home_layout).setVisibility(
                    currentPlayer.getActivePets().isEmpty() ? View.INVISIBLE : View.VISIBLE);

            List<Pet> petList = currentPlayer.getActivePets();
            if (petList != null && !petList.isEmpty()) {
                // TODO so far we only support one
                Pet pet = petList.get(0);

                ImageView petImageView = (ImageView)getView().findViewById(R.id.pet_image_view);
                Picasso.with(getSherlockActivity())
                        .load(pet.getProfile().getImageName())
                        .into(petImageView);
            }

        } else {
            getView().findViewById(R.id.pet_home_empty_layout).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.pet_home_empty_layout).setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adapt_pet:
                goMarket();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
