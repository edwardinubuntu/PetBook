package com.edinubuntu.petlove.util.converter;

import android.util.Log;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.object.Record;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/8/12.
 */
public class RecordsJsonConverter {

    private java.util.List<Record> records;

    public RecordsJsonConverter() {
        records = new ArrayList<Record>();
    }

    public void convert(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray recordsArray = jsonObject.getJSONObject("result").getJSONArray("records");
        for (int index = 0; index < recordsArray.length(); index ++) {
            JSONObject recordObject = recordsArray.getJSONObject(index);

            Record record = new Record();
            record.setAcceptNumber(recordObject.getString("AcceptNum"));
            record.setSterilization(recordObject.getString("IsSterilization"));
            record.setImageName(recordObject.getString("ImageName"));
            record.setName(recordObject.getString("Name"));
            record.setNote(recordObject.getString("Note"));

            Log.d(PetLove.TAG, "Each record: "+record);

            records.add(record);
        }
        Log.d(PetLove.TAG, "Total count: "+records.size());
    }

    public List<Record> getRecords() {
        return records;
    }
}
