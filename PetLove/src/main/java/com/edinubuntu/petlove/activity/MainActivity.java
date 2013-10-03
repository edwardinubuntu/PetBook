package com.edinubuntu.petlove.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.active.ActiveObjectsLoader;
import com.edinubuntu.petlove.adapter.DrawerActionsAdapter;
import com.edinubuntu.petlove.adapter.RecordsAdapter;
import com.edinubuntu.petlove.model.AdaptPetsModel;
import com.edinubuntu.petlove.model.AsyncModel;
import com.edinubuntu.petlove.object.DrawerAction;
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

public class MainActivity extends SherlockActivity
        implements ActiveObjectsLoader<Record> {

    private DrawerLayout drawerLayout;

    private ListView drawerListView;

    private ActionBarDrawerToggle drawerToggle;

    private ArrayList<DrawerAction> drawerActionList;

    private DrawerActionsAdapter drawerListViewAdapter;

    private AdaptPetsModel adaptPetsModel;

    protected GridView petsGridView;

    private RecordsAdapter recordsAdapter;

    private Menu menu;

    private ArrayList<String> dropDownTextList;
    private ArrayAdapter<String> dropDownAdapter;

    private static int GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH;
    private static int GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH;

    private static final int DROP_ITEM_SELECTION_ALL  = 0;
    private static final int DROP_ITEM_SELECTION_DOG  = 1;
    private static final int DROP_ITEM_SELECTION_CAT  = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);

        // Setup Grid view width
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GRID_VIEW_ONE_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels;
        GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels / 3;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

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
                        refreshObjectsToViews(recordList);
                        break;
                    case DROP_ITEM_SELECTION_DOG:
                        java.util.List<Record> dogList = new Select().from(Record.class).where("type='狗'").execute();
                        refreshObjectsToViews(dogList);
                        break;

                    case DROP_ITEM_SELECTION_CAT:
                        java.util.List<Record> catList = new Select().from(Record.class).where("type='貓'").execute();
                        refreshObjectsToViews(catList);
                        break;
                }
                return true;
            }
        });

        petsGridView = (GridView)findViewById(R.id.records_grid_view);
        petsGridView.setColumnWidth(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);
        petsGridView.invalidateViews();

        recordsAdapter = new RecordsAdapter(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<Record>());
        recordsAdapter.setColumnPerPage(GRID_VIEW_TWO_COLUMN_PER_PAGE_WIDTH);

        petsGridView.setAdapter(recordsAdapter);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerListView = (ListView)findViewById(R.id.left_drawer);

        drawerActionList = new ArrayList<DrawerAction>();
        drawerListViewAdapter = new DrawerActionsAdapter(this, drawerActionList);

        drawerListView.setAdapter(drawerListViewAdapter);
        drawerListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             * <p/>
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent   The AdapterView where the click happened.
             * @param view     The view within the AdapterView that was clicked (this
             *                 will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id       The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawerListView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerListView);

                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

                // If user profile them open user
                DrawerAction drawerAction = drawerActionList.get(position);

                if (drawerAction.getActionType() == DrawerAction.ActionType.HOME) {

                }

                if (drawerAction.getActionType() == DrawerAction.ActionType.USER_PROFILE) {
                }
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        // Refresh when all object initialize.
        refreshDrawerActions();

        adaptPetsModel = new AdaptPetsModel();

        java.util.List<Record> recordList = selectObjects(false);
        refreshObjectsToViews(recordList);
        loadObjects(adaptPetsModel, false);

        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        updateRefreshItem();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
            case android.R.id.home:
                boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
                if (drawerOpen) {
                    drawerLayout.closeDrawer(drawerListView);
                } else {
                    drawerLayout.openDrawer(drawerListView);
                }
                break;
            case R.id.action_refresh:
                loadObjects(adaptPetsModel, false);
                break;
            case R.id.action_load_assets_records:
                try {
                    InputStream inputStream = getAssets().open("getAnimals.json");

                    BufferedReader reader = new BufferedReader(  new InputStreamReader(inputStream));
                    String line;
                    StringBuffer contentBuffer = new StringBuffer();
                    while((line = reader.readLine()) != null){
                        contentBuffer.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    Log.d(PetLove.TAG, "Pet file: " + contentBuffer.toString());
                    loadJsonResult(contentBuffer.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(PetLove.TAG, e.toString());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateRefreshItem() {
        if (menu != null) {
            MenuItem refreshItem = menu.findItem(R.id.action_refresh);
            if (this.adaptPetsModel.isLoading()) {
                refreshItem.setActionView(R.layout.indeterminate_progress_action);
            } else {
                refreshItem.setActionView(null);
            }
        }
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
                updateRefreshItem();
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);

                if (!more) {
                    recordsAdapter.clear();
                }

                loadJsonResult(s);
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                super.onFailure(throwable, s);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                asyncModel.setLoading(false);
                updateRefreshItem();
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

    private void loadJsonResult(String s) {
        RecordsJsonConverter recordsJsonConverter = new RecordsJsonConverter();

        ActiveAndroid.beginTransaction();
        try {
            recordsJsonConverter.convert(s);

            List<Record> recordList = recordsJsonConverter.getRecords();
            for (Record record : recordList) {
                // Check before save
                Record savedRecord = new Select().from(Record.class).where("RecordId = ?", record.getRecordId()).executeSingle();
                if (savedRecord == null) {
                    record.save();
                    Log.d(PetLove.TAG, "One record saved. Id: " + record.getRecordId());
                }
            }
            ActiveAndroid.setTransactionSuccessful();

            // Update data to views
            if (recordsAdapter.getCount() == 0 && recordList.size() > 0) {
                refreshObjectsToViews(recordList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private void refreshDrawerActions() {
        drawerActionList.clear();
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_home), DrawerAction.ActionType.HOME));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_profile), DrawerAction.ActionType.USER_PROFILE));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_todo_list), DrawerAction.ActionType.TODO_LIST));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_friends), DrawerAction.ActionType.FRIENDS));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_records), DrawerAction.ActionType.PET_ALL_RECORDS));

        drawerListViewAdapter.setObjectList(drawerActionList);
        drawerListViewAdapter.notifyDataSetChanged();
    }
}
