package com.edinubuntu.petlove.util.manager.badge;

import android.app.Activity;
import android.util.Log;
import com.edinubuntu.petlove.PetLove;
import com.edinubuntu.petlove.object.Badge;
import com.edinubuntu.petlove.util.manager.ParseObjectManager;
import com.edinubuntu.petlove.util.manager.badge.BadgeChecker;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/6.
 */
public class BadgeMostPlayChecker implements BadgeChecker {
    @Override
    public Badge check(final Activity activity) {

        ParseQuery<ParseObject> mostPlayCountUser = ParseQuery.getQuery("UserObject");
        mostPlayCountUser.orderByDescending("PlayCount");
        try {
            List<ParseObject> parseObjects = mostPlayCountUser.find();
            if (parseObjects != null && !parseObjects.isEmpty()) {
                ParseObject mostPlayUser = parseObjects.get(0);
                Log.d(PetLove.TAG, "Most Play Count: " + mostPlayUser.getInt("PlayCount"));
                if (mostPlayUser.getString("AndroidID").equals(ParseObjectManager.getInstance(activity).getUserUniqueId())) {
                    Log.d(PetLove.TAG, "You are the most play user.");
                    return new Badge(Badge.Type.MOST_PLAY_COUNT);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(PetLove.TAG, e.toString());
        }
        return null;
    }
}
