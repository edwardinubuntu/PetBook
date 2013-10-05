package com.edinubuntu.petlove.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.DrawerActionsAdapter;
import com.edinubuntu.petlove.fragment.EventsFragment;
import com.edinubuntu.petlove.fragment.PetHomeFragment;
import com.edinubuntu.petlove.fragment.RecordsFragment;
import com.edinubuntu.petlove.object.DrawerAction;

import java.util.ArrayList;

import static com.edinubuntu.petlove.object.DrawerAction.ActionType.*;

public class MainActivity extends SherlockFragmentActivity
{

    private DrawerLayout drawerLayout;

    private ListView drawerListView;

    private ActionBarDrawerToggle drawerToggle;

    private ArrayList<DrawerAction> drawerActionList;

    private DrawerActionsAdapter drawerListViewAdapter;

    private String fragmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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

                // Update title
                selectDrawerItem(position);
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
                setTitle(fragmentTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(getApplicationContext().getString(getApplicationContext().getApplicationInfo().labelRes));
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        // Refresh when all object initialize.
        refreshDrawerActions();

        if (savedInstanceState == null && !drawerActionList.isEmpty()) {
            selectDrawerItem(0);
        }

        drawerToggle.syncState();
    }

    private void selectDrawerItem(int position) {

        // Display title by default
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        fragmentTitle = drawerActionList.get(position).getTitle();

        setTitle(fragmentTitle);

        // If user profile them open user
        DrawerAction drawerAction = drawerActionList.get(position);

        Fragment fragment = null;
        switch (drawerAction.getActionType()) {
            case HOME: {
                fragment = new PetHomeFragment();
                break;
            }
            case PET_MARKETS: {
                fragment = new RecordsFragment();
                break;
            }
            case PET_EVENTS:
                fragment = new EventsFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerListView.setItemChecked(position, true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void refreshDrawerActions() {
        drawerActionList.clear();
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_home), HOME));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_pet_events), PET_EVENTS));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_badges), BADGES));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_friends), FRIENDS_PET));
        drawerActionList.add(new DrawerAction(getString(R.string.drawer_records), PET_MARKETS));

        drawerListViewAdapter.setObjectList(drawerActionList);
        drawerListViewAdapter.notifyDataSetChanged();
    }
}
