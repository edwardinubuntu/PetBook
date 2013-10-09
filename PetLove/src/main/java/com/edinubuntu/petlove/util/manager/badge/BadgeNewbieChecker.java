package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.object.User;
import com.edinubuntu.petlove.util.manager.UserManager;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class BadgeNewbieChecker implements BadgeChecker {
    @Override
    public Badge check(Activity activity) {

        User user = UserManager.getCurrentPlayer();
        if (!user.getActivePets().isEmpty()) {
            return new Badge(Badge.Type.NEWBIE);
        }
        return null;
    }

    @Override
    public int getResourceName() {
        return R.string.badge_text_newbie;
    }

}
