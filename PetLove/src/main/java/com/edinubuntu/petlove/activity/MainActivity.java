package com.edinubuntu.petlove.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.RecordsAdapter;
import com.edinubuntu.petlove.model.AdaptPetsModel;
import com.edinubuntu.petlove.object.Record;
import com.edinubuntu.petlove.util.converter.RecordsJsonConverter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends SherlockActivity {

    private AdaptPetsModel adaptPetsModel;

    private java.util.List<Record> recordList;

    protected GridView petsGridView;

    private RecordsAdapter recordsAdapter;

    private Menu menu;

    private static int GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH;
    private static int GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);

        // Setup Grid view width
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels;
        GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels / 3;
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        updateRefreshItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadModel(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        recordList = new ArrayList<Record>();

        petsGridView = (GridView)findViewById(R.id.records_grid_view);
        petsGridView.setColumnWidth(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);
        petsGridView.invalidateViews();

        recordsAdapter = new RecordsAdapter(MainActivity.this, android.R.layout.simple_list_item_1, recordList);
        recordsAdapter.setColumnPerPage(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);

        petsGridView.setAdapter(recordsAdapter);

        adaptPetsModel = new AdaptPetsModel();

        loadModel(false);
    }

    private void loadModel(final boolean more) {
        adaptPetsModel.load(new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                adaptPetsModel.setLoading(true);
                updateRefreshItem();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                if (!more) {
                    recordsAdapter.clear();
                }

                RecordsJsonConverter recordsJsonConverter = new RecordsJsonConverter();
                try {
                    recordsJsonConverter.convert(s);

                    recordList = recordsJsonConverter.getRecords();

                    for (Record record : recordList) {
                        recordsAdapter.insert(record, recordsAdapter.getCount());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(PetLove.TAG, e.toString());
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                recordsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                adaptPetsModel.setLoading(false);
                updateRefreshItem();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void updateRefreshItem() {
        if (menu != null) {
            MenuItem refreshItem = menu.findItem(R.id.action_refresh);
            if (this.adaptPetsModel.isLoading()) {
                refreshItem.setActionView(R.layout.indeterminate_progress_action);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }
}
