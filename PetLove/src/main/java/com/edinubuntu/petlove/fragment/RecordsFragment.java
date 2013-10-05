package com.edinubuntu.petlove.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.active.ActiveObjectsLoader;
import com.edinubuntu.petlove.adapter.RecordsAdapter;
import com.edinubuntu.petlove.model.AdaptPetsModel;
import com.edinubuntu.petlove.model.AsyncModel;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.Record;
import com.edinubuntu.petlove.util.converter.RecordsJsonConverter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/3.
 */
public class RecordsFragment extends SherlockFragment implements ActiveObjectsLoader<Record> {

    private GridView petsGridView;

    private AdaptPetsModel adaptPetsModel;

    private RecordsAdapter recordsAdapter;

    private static int GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH;
    private static int GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH;

    private static final int DROP_ITEM_SELECTION_ALL  = 0;
    private static final int DROP_ITEM_SELECTION_DOG  = 1;
    private static final int DROP_ITEM_SELECTION_CAT  = 2;

    private ArrayList<String> dropDownTextList;
    private ArrayAdapter<String> dropDownAdapter;

    private Menu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup Grid view width
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels;
        GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels / 3;

        adaptPetsModel = new AdaptPetsModel();

        ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        dropDownTextList = new ArrayList<String>();
        dropDownTextList.add(getString(R.string.dropdown_selection_all));
        dropDownTextList.add(getString(R.string.dropdown_selection_dog));
        dropDownTextList.add(getString(R.string.dropdown_selection_cat));

        dropDownAdapter = new ArrayAdapter<String>(
                actionBar.getThemedContext(), android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, dropDownTextList);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        actionBar.setListNavigationCallbacks(dropDownAdapter, new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                switch (itemPosition) {
                    case DROP_ITEM_SELECTION_ALL:
                        java.util.List<Record> recordList = new Select().from(Record.class).execute();
                        if (recordList.isEmpty()) {
                            askToDownloadRecords();
                        }
                        refreshObjectsToViews(recordList);
                        Event visitMarketEvent = new Event(Event.Action.VISIT_MARKET_ALL);
                        visitMarketEvent.save();

                        break;
                    case DROP_ITEM_SELECTION_DOG:
                        java.util.List<Record> dogList = new Select().from(Record.class).where("type='"+
                                getString(R.string.record_type_dog)
                                +"'").execute();
                        refreshObjectsToViews(dogList);

                        new Event(Event.Action.VISIT_MARKET_DOG).save();

                        break;

                    case DROP_ITEM_SELECTION_CAT:
                        java.util.List<Record> catList = new Select().from(Record.class).where("type='" +
                                getString(R.string.record_type_cat)
                                + "'").execute();
                        refreshObjectsToViews(catList);

                        new Event(Event.Action.VISIT_MARKET_CAT).save();

                        break;
                }
                return true;
            }
        });

        setHasOptionsMenu(true);
    }

    private void askToDownloadRecords() {
        new AlertDialog.Builder(getSherlockActivity())
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_records, container, false);

        petsGridView = (GridView)rootView.findViewById(R.id.records_grid_view);
        petsGridView.setColumnWidth(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);
        petsGridView.invalidateViews();

        recordsAdapter = new RecordsAdapter(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Record>());
        recordsAdapter.setColumnPerPage(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);

        petsGridView.setAdapter(recordsAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_records, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadObjects(adaptPetsModel, false);
                break;
            case R.id.action_load_https_records:
                loadObjects(adaptPetsModel, false);
                break;
            case R.id.action_delete_records:
                new Delete().from(Record.class).execute();
                Toast.makeText(getSherlockActivity(), getString(R.string.action_delete_successfully), Toast.LENGTH_LONG).show();

                java.util.List<Record> recordList = new Select().from(Record.class).execute();
                if (recordList.isEmpty()) {
                    askToDownloadRecords();
                }

                break;
            case R.id.action_load_assets_records:
                try {
                    InputStream inputStream = getSherlockActivity().getAssets().open("data.json");

                    BufferedReader reader = new BufferedReader(  new InputStreamReader(inputStream));
                    String line;
                    StringBuffer contentBuffer = new StringBuffer();
                    while((line = reader.readLine()) != null){
                        contentBuffer.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    Log.d(PetLove.TAG, "Pet file: " + contentBuffer.toString());
                    loadJsonResult(contentBuffer.toString(), true);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(PetLove.TAG, e.toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<Record> selectObjects(boolean more) {
        java.util.List<Record> recordList = new Select().from(Record.class).execute();
        return recordList;
    }

    @Override
    public void loadObjects(final AsyncModel asyncModel, final boolean more) {
        asyncModel.load(new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                asyncModel.setLoading(true);
                updateRefreshItem(asyncModel.isLoading());
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (!more) {
                    recordsAdapter.clear();
                }
                loadJsonResult(s, true);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                asyncModel.setLoading(false);
                updateRefreshItem(asyncModel.isLoading());
            }
        });
    }


    @Override
    public void refreshObjectsToViews(List<Record> recordList) {
        recordsAdapter.clear();
        for (Record record : recordList) {
            recordsAdapter.insert(record, recordsAdapter.getCount());
        }
        recordsAdapter.notifyDataSetChanged();
    }

    private void updateRefreshItem(boolean isLoading) {
        if (menu != null) {
            MenuItem refreshItem = menu.findItem(R.id.action_refresh);
            if (isLoading) {
                refreshItem.setActionView(R.layout.indeterminate_progress_action);
            } else {
                refreshItem.setActionView(null);
            }
        }
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

                Toast.makeText(getSherlockActivity(), getString(R.string.records_download_saved) + savedCount, Toast.LENGTH_LONG).show();
            }

            // Update data to views
            if (recordsAdapter.getCount() == 0 && recordList.size() > 0) {
                refreshObjectsToViews(recordList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

}
