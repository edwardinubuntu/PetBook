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
import android.widget.RelativeLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.DrawerActionsAdapter;
import com.edinubuntu.petlove.fragment.*;
import com.edinubuntu.petlove.object.DrawerAction;
import com.edinubuntu.petlove.object.Event;
import com.edinubuntu.petlove.object.User;
import com.edinubuntu.petlove.util.manager.ParseObjectManager;
import com.edinubuntu.petlove.util.manager.UserManager;

import java.util.ArrayList;
import java.util.List;

import static com.edinubuntu.petlove.object.DrawerAction.ActionType.*;

public class MainActivity extends SherlockFragmentActivity
{

    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private RelativeLayout drawerListViewLayout;

    /*
    * Upper
     */
    private List<DrawerAction> drawerUpperActionList;
    private DrawerActionsAdapter drawerUpperListViewAdapter;
    private ListView drawerUpperListView;

    /*
    * Bottom
     */
    private List<DrawerAction> drawerBottomActionList;
    private ListView drawerBottomListView;
    private DrawerActionsAdapter drawerBottomListViewAdapter;

    private String fragmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ParseObjectManager.getInstance(this).initialize();

        // Initialize create user profile
        User currentPlayer = UserManager.getCurrentPlayer();
        if (currentPlayer == null) {
            currentPlayer = new User(User.Type.PLAYER);
            currentPlayer.save();

            new Event(Event.Action.USER_PROFILE_CREATE).save();
        }
        ParseObjectManager.getInstance(this).saveAndUpdateUser(currentPlayer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        drawerListViewLayout = (RelativeLayout)findViewById(R.id.left_drawer_layout);

        // Upper
        drawerUpperListView = (ListView)findViewById(R.id.left_top_list_view);
        drawerUpperActionList = new ArrayList<DrawerAction>();
        drawerUpperListViewAdapter = new DrawerActionsAdapter(this, drawerUpperActionList);
        drawerUpperListView.setAdapter(drawerUpperListViewAdapter);
        drawerUpperListView.setOnItemClickListener(new ListView.OnItemClickListener() {
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

                drawerUpperListView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerListViewLayout);

                // Update title
                selectDrawerItem(drawerUpperListView, position);
            }
        });

        // Bottom
        drawerBottomListView = (ListView)findViewById(R.id.left_bottom_list_view);
        drawerBottomActionList = new ArrayList<DrawerAction>();
        drawerBottomListViewAdapter = new DrawerActionsAdapter(this, drawerBottomActionList);
        drawerBottomListView.setAdapter(drawerBottomListViewAdapter);
        drawerBottomListView.setOnItemClickListener(new ListView.OnItemClickListener() {
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

                drawerBottomListView.setItemChecked(position, true);
                drawerLayout.closeDrawer(drawerListViewLayout);

                // Update title
                selectDrawerItem(drawerBottomListView, position);
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

        if (savedInstanceState == null && !drawerUpperActionList.isEmpty()) {
            selectDrawerItem(drawerUpperListView, 0);
        }

        drawerToggle.syncState();
    }

    private void selectDrawerItem(ListView listView, int position) {

        // Display title by default
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Fragment fragment = null;
        if (listView == drawerUpperListView) {
            // If user profile them open user
            DrawerAction drawerAction = drawerUpperActionList.get(position);
            fragmentTitle = drawerAction.getTitle();
            setTitle(fragmentTitle);
            switch (drawerAction.getActionType()) {
                case HOME: {
                    fragment = new PetHomeFragment();
                    break;
                }
                case PET_MARKETS: {
                    fragment = new RecordsFragment();
                    break;
                }
                case BADGES: {
                    fragment = new BadgesFragment();
                    break;
                }
                case PET_EVENTS:
                    fragment = new EventsFragment();
                    break;
                case KNOWLEDGE_CONTENTS:
                    fragment = new KnowContentFragment();
                    break;
            }

            drawerUpperListView.setItemChecked(position, true);
        } else if (listView == drawerBottomListView) {

            DrawerAction drawerAction = drawerBottomActionList.get(position);
            switch (drawerAction.getActionType()) {
                case SETTINGS: {
                    Intent settingsIntent = new Intent(this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;
                }
                default:
                    fragmentTitle = drawerAction.getTitle();
                    setTitle(fragmentTitle);
                    break;
            }

            drawerBottomListView.setItemChecked(position, true);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
                boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListViewLayout);
                if (drawerOpen) {
                    drawerLayout.closeDrawer(drawerListViewLayout);
                } else {
                    drawerLayout.openDrawer(drawerListViewLayout);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void refreshDrawerActions() {
        drawerUpperActionList.clear();
        drawerUpperActionList.add(new DrawerAction(getString(R.string.drawer_home), HOME));
        drawerUpperActionList.add(new DrawerAction(getString(R.string.drawer_pet_events), PET_EVENTS));
        drawerUpperActionList.add(new DrawerAction(getString(R.string.drawer_knowledge_content), KNOWLEDGE_CONTENTS));
        drawerUpperActionList.add(new DrawerAction(getString(R.string.drawer_badges), BADGES));
//        drawerUpperActionList.add(new DrawerAction(getString(R.string.drawer_records), PET_MARKETS));
        drawerUpperListViewAdapter.setObjectList(drawerUpperActionList);
        drawerUpperListViewAdapter.notifyDataSetChanged();

        drawerBottomActionList.clear();
        drawerBottomActionList.add(new DrawerAction(getString(R.string.drawer_friends), FRIENDS_PET));
        drawerBottomActionList.add(new DrawerAction(getString(R.string.drawer_settings), SETTINGS));
        drawerBottomListViewAdapter.setObjectList(drawerBottomActionList);
        drawerBottomListViewAdapter.notifyDataSetChanged();
    }
}
