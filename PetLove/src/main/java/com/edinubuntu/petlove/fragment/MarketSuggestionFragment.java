package com.edinubuntu.petlove.fragment;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragment;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.object.Record;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketSuggestionFragment extends SherlockFragment {

    private Map<String, String> queryMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, String> queryMap) {
        this.queryMap = queryMap;
    }
}
