package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import com.edinubuntu.petlove.object.Badge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/6.
 */
public class BadgeManager {

    private static BadgeManager instance;

    private Activity activity;

    private List<BadgeChecker> badgeCheckers;

    private  ProgressListener progressListener;

    public static synchronized BadgeManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new BadgeManager(activity);
        }
        return instance;
    }

    public BadgeManager(Activity activity) {
        this.activity = activity;

        badgeCheckers = new ArrayList<BadgeChecker>();
        badgeCheckers.add(new BadgeNewbieChecker());
        badgeCheckers.add(new BadgeMostPlayChecker());
        badgeCheckers.add(new BadgeTopInitialPlayChecker());
    }

    public List<Badge> checkUserBadges() {
        List<Badge> badges = new ArrayList<Badge>();

        for (BadgeChecker badgeChecker : badgeCheckers) {
            progressListener.onCheck(badgeChecker);
            Badge earnedBadge = badgeChecker.check(this.activity);
            if  (earnedBadge != null) {
                badges.add(earnedBadge);
            }
        }
        return badges;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public interface ProgressListener {
        public void onCheck(BadgeChecker badgeChecker);
    }
}
