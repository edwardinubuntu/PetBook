package com.edinubuntu.petlove.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.fragment.MarketChoiceFragment;
import com.edinubuntu.petlove.model.AdaptPetsModel;
import com.edinubuntu.petlove.model.AsyncModel;
import com.edinubuntu.petlove.object.Record;
import com.edinubuntu.petlove.util.converter.RecordsJsonConverter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class MarketActivity extends SherlockFragmentActivity {

    private AdaptPetsModel adaptPetsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MarketChoiceFragment()).commit();

        // TODO move to manager
        adaptPetsModel = new AdaptPetsModel();
        java.util.List<Record> recordList = new Select().from(Record.class).execute();
        if (recordList.isEmpty()) {
            askToDownloadRecords();
        }
    }

    private void askToDownloadRecords() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.records_download_from_https_title))
                .setMessage(getResources().getString(R.string.records_download_from_https_message))
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    loadObjects(adaptPetsModel, false);
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.market_finish_confirm_title))
                        .setMessage(getString(R.string.market_finish_confirm_message))
                        .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadObjects(final AsyncModel asyncModel, final boolean more) {
        asyncModel.load(new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                asyncModel.setLoading(true);
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                loadJsonResult(s, true);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                asyncModel.setLoading(false);
            }
        });
    }

    private void loadJsonResult(String s, boolean saveData) {
        RecordsJsonConverter recordsJsonConverter = new RecordsJsonConverter();

        ActiveAndroid.beginTransaction();
        try {
            recordsJsonConverter.convert(s);

            List<Record> recordList = recordsJsonConverter.getRecords();

            if (saveData) {
                int savedCount = 0;
                for (Record record : recordList) {
                    // Check before save
                    Record savedRecord = new Select().from(Record.class).where("RecordId = ?", record.getRecordId()).executeSingle();
                    if (savedRecord == null) {
                        record.save();
                        savedCount++;
                        Log.d(PetLove.TAG, "One record saved. Id: " + record.getRecordId());
                    }
                }
                ActiveAndroid.setTransactionSuccessful();

                Toast.makeText(this, getString(R.string.records_download_saved) + savedCount, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
