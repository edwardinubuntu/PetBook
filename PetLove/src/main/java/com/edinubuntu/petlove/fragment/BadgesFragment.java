package com.edinubuntu.petlove.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.SherlockFragment;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.util.manager.badge.BadgeManager;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/6.
 */
public class BadgesFragment extends SherlockFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CheckBadgesTask checkBadgesTask = new CheckBadgesTask();
        checkBadgesTask.execute((Void[]) null);

    }

    public class CheckBadgesTask extends AsyncTask<Void, Void, List<Badge>> {

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
            Log.d(PetLove.TAG, "You earned badges: "+ earnedBadges.toString());
            return earnedBadges;
        }
    }
}
