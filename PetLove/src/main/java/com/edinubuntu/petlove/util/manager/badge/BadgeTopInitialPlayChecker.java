package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import android.util.Log;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.util.manager.ParseObjectManager;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class BadgeTopInitialPlayChecker implements BadgeChecker {
    @Override
    public Badge check(Activity activity) {
        ParseQuery<ParseObject> mostPlayCountUser = ParseQuery.getQuery("UserObject");
        mostPlayCountUser.orderByAscending("UserCreatedAt");
        mostPlayCountUser.setLimit(10);
        try {
            List<ParseObject> parseObjects = mostPlayCountUser.find();
            if (parseObjects != null && !parseObjects.isEmpty()) {
                ParseObject mostPlayUser = parseObjects.get(0);
                Log.d(PetLove.TAG, "Most Play Count: " + mostPlayUser.getInt("PlayCount"));
                if (mostPlayUser.getString("AndroidID").equals(ParseObjectManager.getInstance(activity).getUserUniqueId())) {
                    Log.d(PetLove.TAG, "You are the top 10 oldest play user.");
                    return new Badge(Badge.Type.TOP_10_OLD);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
        }
        return null;
    }

    @Override
    public int getResourceName() {
        return R.string.badge_text_top_ten_old_play;
    }
}
