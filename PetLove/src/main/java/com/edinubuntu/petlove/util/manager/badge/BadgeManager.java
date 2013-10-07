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

    public static synchronized BadgeManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new BadgeManager(activity);
        }
        return instance;
    }

    public BadgeManager(Activity activity) {
        this.activity = activity;

        badgeCheckers = new ArrayList<BadgeChecker>();
        badgeCheckers.add(new BadgeMostPlayChecker());
    }

    public List<Badge> checkUserBadges() {
        List<Badge> badges = new ArrayList<Badge>();

        for (BadgeChecker badgeChecker : badgeCheckers) {
            Badge earnedBadge = badgeChecker.check(this.activity);
            if  (earnedBadge != null) {
                badges.add(earnedBadge);
            }
        }
        return badges;
    }
}
