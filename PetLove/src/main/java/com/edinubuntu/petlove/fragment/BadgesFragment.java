package com.edinubuntu.petlove.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.adapter.BadgesAdapter;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.util.manager.badge.BadgeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/6.
 */
public class BadgesFragment extends SherlockFragment {

    private GridView badgesGridView;

    private BadgesAdapter badgesAdapter;

    private List<Badge> badgeList;

    private ProgressDialog waitingDialog;

    private static int GRID_VIEW_FOUR_COLUMN_PER_PAGE_WIDTH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        badgeList = new ArrayList<Badge>();

        waitingDialog = new ProgressDialog(getSherlockActivity());
        waitingDialog.setTitle(getString(R.string.badge_check_waiting_title));
        waitingDialog.setMessage(getString(R.string.waiting));
        waitingDialog.setIndeterminate(false);
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setCancelable(false);

        setHasOptionsMenu(true);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        GRID_VIEW_FOUR_COLUMN_PER_PAGE_WIDTH = metrics.widthPixels / (4 + 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_badges_main, container, false);

        badgesGridView = (GridView)rootView.findViewById(R.id.badges_grid_view);
        badgesGridView.setColumnWidth(GRID_VIEW_FOUR_COLUMN_PER_PAGE_WIDTH);

        badgesAdapter = new BadgesAdapter(getSherlockActivity(), android.R.layout.simple_list_item_1, badgeList);
        badgesGridView.setAdapter(badgesAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Badge> savedBadge = new Select().from(Badge.class).execute();
        if (savedBadge == null || savedBadge.isEmpty()) {
            checkBadges();
        } else {
            this.badgeList.clear();
            this.badgeList.addAll(savedBadge);
            badgesAdapter.notifyDataSetChanged();
        }

    }

    private void checkBadges() {
        AsyncTask checkBadgesTask = new AsyncTask<Void, Void, List<Badge>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                waitingDialog.show();
            }

            /**
             * Override this method to perform a computation on a background thread. The
             * specified parameters are the parameters passed to {@link #execute}
             * by the caller of this task.
             * <p/>
             * This method can call {@link #publishProgress} to publish updates
             * on the UI thread.
             *
             * @param params The parameters of the task.
             * @return A result, defined by the subclass of this task.
             * @see #onPreExecute()
             * @see #onPostExecute
             * @see #publishProgress
             */
            @Override
            protected List<Badge> doInBackground(Void... params) {

                List<Badge> earnedBadges = BadgeManager.getInstance(getSherlockActivity()).checkUserBadges();
                Log.d(PetLove.TAG, "You earned badges: " + earnedBadges.toString());

                return earnedBadges;
            }

            @Override
            protected void onPostExecute(List<Badge> badges) {
                super.onPostExecute(badges);

                // TODO should we remove old one?
                new Delete().from(Badge.class).execute();
                for (Badge eachBadge : badges) {
                    // Check before save
                    Badge savedRecord = new Select().from(Badge.class).where("Type = ?", eachBadge.getType()).executeSingle();
                    if (savedRecord == null) {
                        eachBadge.save();
                    }
                }

                badgeList.clear();
                badgeList.addAll(badges);
                badgesAdapter.notifyDataSetChanged();

                waitingDialog.dismiss();
            }
        };
        checkBadgesTask.execute((Void[]) null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_badges, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                checkBadges();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
