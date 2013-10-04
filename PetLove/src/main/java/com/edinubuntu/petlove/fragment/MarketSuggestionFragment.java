package com.edinubuntu.petlove.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.DisplayText;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.MarketSuggestionsPagerAdapter;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.Record;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketSuggestionFragment extends SherlockFragment {

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

        List<Record> recordList = new Select().from(Record.class)
                .where(whereBuffer.toString())
                .orderBy("RANDOM()")
                .limit(10)
                .execute();
        Log.d(PetLove.TAG, "Number suggest :" + recordList.size());
        Log.d(PetLove.TAG, "We suggest :" + recordList.toString());

        marketSuggestionsPagerAdapter.setRecords(recordList);
        marketSuggestionsPagerAdapter.notifyDataSetChanged();

        new AlertDialog.Builder(getSherlockActivity())
                .setTitle(getString(R.string.alert_suit_title) + DisplayText.newInstance(getSherlockActivity()).getType(queryMap.get("Type")))
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
