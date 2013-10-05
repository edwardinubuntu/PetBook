package com.edinubuntu.petlove.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.util.manager.DisplayTextManager;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.MarketSuggestionsPagerAdapter;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.Pet;
import com.edinubuntu.petlove.object.Record;
import com.edinubuntu.petlove.object.User;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketSuggestionFragment extends SherlockFragment {

    private List<Record> recordList;

    private Map<String, String> queryMap;

    private ViewPager suggestionsViewPager;

    private MarketSuggestionsPagerAdapter marketSuggestionsPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_market_suggestion, container, false);

        suggestionsViewPager = (ViewPager)rootView.findViewById(R.id.market_record_content_pager);
        marketSuggestionsPagerAdapter = new MarketSuggestionsPagerAdapter(getFragmentManager());
        suggestionsViewPager.setAdapter(marketSuggestionsPagerAdapter);

        Button choicePetButton = (Button)rootView.findViewById(R.id.market_record_pick_button);
        choicePetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getSherlockActivity())
                        .setTitle("選擇寵物")
                        .setMessage("一次只能僅能認養一隻，不能換喔！最後確認嗎？")
                        .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Record currentRecord = recordList.get(suggestionsViewPager.getCurrentItem());

                                // Save record to pet
                                Pet adaptPet = new Pet();
                                adaptPet.setName(currentRecord.getName());
                                adaptPet.setProfile(currentRecord);

                                // Get Current Player Profile
                                User currentPlayer = new Select().from(User.class).where("Type = '" +User.Type.PLAYER+ "'").executeSingle();
                                adaptPet.setOwner(currentPlayer);
                                adaptPet.save();

                                // Finish and back
                                Toast.makeText(getSherlockActivity(), "認養完畢，恭喜新成員到來。", Toast.LENGTH_LONG).show();
                                getSherlockActivity().finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Event(Event.Action.VISIT_MARKET_SUGGESTIONS).save();

        StringBuffer whereBuffer = new StringBuffer();
        Iterator it = queryMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            whereBuffer.append(pairs.getKey() + "= '"+pairs.getValue()+"'");

            if (it.hasNext()) {
                whereBuffer.append(" and ");
            }
        }

        recordList = new Select().from(Record.class)
                .where(whereBuffer.toString())
                .orderBy("RANDOM()")
                .limit(10)
                .execute();
        Log.d(PetLove.TAG, "Number suggest :" + recordList.size());
        Log.d(PetLove.TAG, "We suggest :" + recordList.toString());

        marketSuggestionsPagerAdapter.setRecords(recordList);
        marketSuggestionsPagerAdapter.notifyDataSetChanged();

        new AlertDialog.Builder(getSherlockActivity())
                .setTitle(getString(R.string.alert_suit_title) + DisplayTextManager.newInstance(getSherlockActivity()).getType(queryMap.get("Type")))
                .setMessage(getString(R.string.alert_suit_message_1)
                        + recordList.size() +
                        getString(R.string.alert_suit_message_2))

                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }
}
