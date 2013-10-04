package com.edinubuntu.petlove.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.edinubuntu.petlove.fragment.MarketSuggestProfileFragment;
import com.edinubuntu.petlove.object.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketSuggestionsPagerAdapter extends FragmentPagerAdapter {

    private List<Record> records;

    public MarketSuggestionsPagerAdapter(FragmentManager fm) {
        super(fm);
        records = new ArrayList<Record>();
    }

    @Override
    public Fragment getItem(int i) {
        return MarketSuggestProfileFragment.newInstance(records.get(i));
    }

    @Override
    public int getCount() {
        return records.size();
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
