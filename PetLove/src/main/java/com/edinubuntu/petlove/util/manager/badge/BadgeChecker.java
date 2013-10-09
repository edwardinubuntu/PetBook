package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import com.edinubuntu.petlove.object.Badge;

/**
 * Created by edward_chiang on 13/10/6.
 */
public interface BadgeChecker {
    public Badge check(Activity activity);

    public int getResourceName();
}
